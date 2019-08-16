#!/usr/bin/python
# coding: UTF-8
import xml.etree.ElementTree as ET
import sys, os, time


def check_command(result, message):
    if result != 0:
        print(message)
        os.system("exit")


# 创建临时文件
def create_temp_file():
    temp_path = os.getcwd() + "/../temp"
    if os.path.exists(temp_path):
        print("[inject] 清空temp文件夹")
        os.system("rm -rf " + temp_path + "/*")
    else:
        print("[inject] 创建temp文件夹")
        os.system("mkdir " + temp_path)


# 获取需要注入的java文件
def src_java_path():
    src_path = os.getcwd() + "/../src"
    source_path = os.getcwd() + "/../temp/sources.txt"
    if os.path.exists(src_path):
        print("[inject] 查找src下所有Java文件，路径保存到 " + source_path)
        os.system("find " + src_path + " -name *.java > " + source_path)
    else:
        print("[inject] 查找src文件失败，请在根目录src下放入需要注入的java代码")


# java转class
def javac_class():
    source_path = os.getcwd() + "/../temp/sources.txt"
    output_class_file = os.getcwd() + "/../temp/outputClass"
    if (os.path.exists(output_class_file) == False):
        os.system("mkdir " + output_class_file)
    os.system("javac -classpath android.jar @" + source_path + " -d " + output_class_file)
    print("[inject] 将sources.txt里面对应的java文件编译成class文件")


# class转dex
def dex_class():
    temp_path = os.getcwd() + "/../temp"
    os.system("dx --dex --output=" + temp_path + "/apkInject.dex " + temp_path + "/outputClass")
    print("[inject] 将outputClass文件转成apkInject.dex")


# dex转smali
def smali_dex():
    temp_path = os.getcwd() + "/../temp"
    os.system("java -jar baksmali.jar d  " + temp_path + "/apkInject.dex -o " + temp_path + "/outputSmali/")
    print("[inject] 将apkInject.dex转成smali")


# 获取准备反编译的apk
def apk_src_path():
    apks_path = []
    cur_path = os.getcwd()
    os.chdir("..")
    fs = os.listdir(os.getcwd())
    for f in fs:
        suffix = os.path.splitext(f)[1]
        if '.apk' == suffix:
            apks_path.append(os.getcwd() + "/" + f)
    os.chdir(cur_path)
    return apks_path


# 用apktool开始反编译apk
def apktool_d(apk_path):
    print("[inject] 开始反编译apk ", apk_path)
    file_name = os.path.splitext(os.path.split(apk_path)[1])[0]
    file_path = os.path.split(apk_path)[0] + "/temp/" + file_name  # 反编译后的文件路径
    if False == os.path.exists(file_path):
        os.system('apktool -f d ' + apk_path + " -o " + file_path)

    print("[inject] 完成反编译apk 路径:", file_path)
    return file_path


# 拷贝smali文件到apk里面
def copy_smali_apk(apk_file_path):
    temp_path = os.getcwd() + "/../temp"
    src_smali = temp_path + "/outputSmali/*"
    apk_smali = apk_file_path + "/smali"
    os.system("cp -r " + src_smali + " " + apk_smali)
    print("[inject] 拷贝注入的smali到目标apk里面")


# 获取application对应的文件
def get_app_class(apk_file_path):
    manifest = apk_file_path + "/AndroidManifest.xml";
    tree = ET.parse(manifest)
    root = tree.getroot()

    for child in root:
        if child.tag == "application":
            attrib = child.attrib
            # print(" attrib", attrib)
            for att in attrib:
                if "name" in att:
                    # Application的包名
                    return attrib.get(att)
    return None


# 查找application所在路径
def find_application_path(apk_file_path, app_class_name):
    print("[inject] 开始查找Application所在路径")
    ps = app_class_name.split('.')
    app_smali_path = apk_file_path + "/smali"
    for p in ps:
        app_smali_path += "/" + p
    app_smali_path += ".smali"
    if os.path.exists(app_smali_path):
        print("[inject] 查找Application成功，路径为 " + app_smali_path)
        return app_smali_path
    else:
        print("[inject] 查找Application失败，请人工核实")
        return None


# 修改application,里面调用注入的方法
def modify_application_class(application_path):
    print("[inject] Application onCreate里面方法添加方法调用")
    file_data = ""
    with open(application_path, "r", encoding="utf-8") as f:
        flag = False
        count = 0
        for line in f:
            if ".method public onCreate()V" in line:
                flag = True
            if flag:
                count += 1
            if count >= 6:
                file_data += "\n    invoke-static {p0}, Lcom/playin/hook/AudioHook;->init(Landroid/content/Context;)V\n"
                flag = False
                count = 0
            file_data += line
    f.close()
    with open(application_path, "w", encoding="utf-8") as f:
        f.write(file_data)
    f.close()


# 用apktool打包apk
def apktool_b(apk_file_path):
    print("[inject] 使用apktool重新打包")
    os.system('apktool b ' + apk_file_path)
    fs = os.listdir(apk_file_path + "/dist")
    for f in fs:
        suffix = os.path.splitext(f)[1]
        if ('.apk' == suffix):
            return apk_file_path + "/dist/" + f
        else:
            return None


# 给新生产的apk签名
def sign_apk(apk_path):
    print("[inject] 给apk重新签名")
    temp_path = os.getcwd() + "/../temp"
    new_apk_path = temp_path + "/sign_" + time.strftime('%m%d') + ".apk "
    cmd = "jarsigner -verbose -keystore playin.jks -signedjar " + new_apk_path + apk_path + " playin -storepass playin"
    os.system(cmd)
    return new_apk_path


def main():
    create_temp_file()
    src_java_path()
    javac_class()
    dex_class()
    smali_dex()

    apks_path = apk_src_path()
    if len(apks_path) == 0:
        print("[inject] 请在根目录放入需要注入的apk文件")
    elif len(apks_path) != 1:
        print("[inject] 根目录只能包含一个apk文件")
    else:
        apk_file_path = apktool_d(apks_path[0])
        copy_smali_apk(apk_file_path)
        app_package_name = get_app_class(apk_file_path)
        print("[inject] 获取到Application对应的包名 " + app_package_name)
        application_path = find_application_path(apk_file_path, app_package_name)
        if application_path:
            modify_application_class(application_path)

        new_apk_path = apktool_b(apk_file_path)
        new_apk_path = sign_apk(new_apk_path)
        print("[inject] 签名成功，路径为: " + new_apk_path)

main()
