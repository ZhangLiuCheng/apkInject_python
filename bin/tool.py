#!/usr/bin/python
# coding: UTF-8
import xml.etree.ElementTree as ET
import os
import time


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


# 创建临时文件
def create_temp_file():
    temp_path = os.getcwd() + "/../temp"
    if os.path.exists(temp_path):
        print("[inject] temp文件夹已存在")
        # print("[inject] 清空temp文件夹")
        # os.system("rm -rf " + temp_path + "/*")
    else:
        print("[inject] 创建temp文件夹")
        os.system("mkdir " + temp_path)


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
        result = os.system('apktool -f d ' + apk_path + " -o " + file_path)
        check_command(result)

    print("[inject] 完成反编译apk 路径:", file_path)
    return file_path


# 用apktool打包apk
def apktool_b(apk_file_path):
    print("[inject] 使用apktool重新打包")
    result = os.system('apktool b ' + apk_file_path)
    check_command(result)
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
    result = os.system(cmd)
    check_command(result)
    return new_apk_path


def exec_cmd(cmd):
    r = os.popen(cmd)
    text = r.read()
    r.close()
    return text.strip()


def check_command(result):
    if result != 0:
        # os.system("pause")
        print("!!!!!!!!!!!!   命令执行异常  !!!!!!!!!!!!!")
        exit()
        # os.system("exit 0")


def sign_apk_test(apk_path):
    print("[inject] 给apk重新签名")
    temp_path = os.getcwd() + "/../"
    new_apk_path = temp_path + "/sign_" + time.strftime('%m%d') + ".apk "
    cmd = "jarsigner -verbose -keystore playin.jks -signedjar " + new_apk_path + apk_path + " playin -storepass playin"
    result = os.system(cmd)
    check_command(result)
    return new_apk_path
