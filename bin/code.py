#!/usr/bin/python
# coding: UTF-8
import os
import tool


# 获取需要注入的java文件
def src_java_path():
    src_path = os.getcwd() + "/src"
    source_path = os.getcwd() + "/../temp/sources.txt"
    if os.path.exists(src_path):
        print("[inject] 查找src下所有Java文件，路径保存到 " + source_path)
        result = os.system("find " + src_path + " -name *.java > " + source_path)
        tool.check_command(result)
    else:
        print("[inject] 查找src文件失败，请在根目录src下放入需要注入的java代码")


# java转class
def javac_class():
    source_path = os.getcwd() + "/../temp/sources.txt"
    output_class_file = os.getcwd() + "/../temp/outputClass"
    if (os.path.exists(output_class_file) == False):
        os.system("mkdir " + output_class_file)
    result = os.system("javac -classpath jars/android.jar @" + source_path + " -d " + output_class_file)
    tool.check_command(result)
    print("[inject] 将sources.txt里面对应的java文件编译成class文件")


# class转dex
def dex_class():
    temp_path = os.getcwd() + "/../temp"
    result = os.system("dx --dex --output=" + temp_path + "/apkInject.dex " + temp_path + "/outputClass")
    tool.check_command(result)
    print("[inject] 将outputClass文件转成apkInject.dex")


# dex转smali
def smali_dex():
    temp_path = os.getcwd() + "/../temp"
    result = os.system("java -jar jars/baksmali.jar d  " + temp_path + "/apkInject.dex -o " + temp_path + "/outputSmali/")
    tool.check_command(result)
    print("[inject] 将apkInject.dex转成smali")


# 拷贝libs到apk里面
def copy_libs_apk(apk_file_path):
    temp_path = os.getcwd() + "/../temp"
    result = os.system("cp -r " + "libs/armeabi-v7a/* " + apk_file_path + "/lib/armeabi-v7a")
    # result = os.system("cp -r " + "libs/arm64-v8a/* " + apk_file_path + "/lib/arm64-v8a")

    print("[inject] 拷贝libs到目标apk里面")

    os.system("rm -rf " + apk_file_path + "/lib/armeabi")
    os.system("rm -rf " + apk_file_path + "/lib/x86")
    os.system("rm -rf " + apk_file_path + "/lib/x86_64")
    os.system("rm -rf " + apk_file_path + "/lib/arm64-v8a")


# 拷贝smali文件到apk里面
def copy_smali_apk(apk_file_path):
    # print(apk_file_path)
    # c3 = apk_file_path + "/smali_classes4"
    # print("------>  ", c3,  os.path.exists(c3))

    temp_path = os.getcwd() + "/../temp"
    src_smali = temp_path + "/outputSmali/*"

    # 获取samli路径, python不熟悉又赶时间，这边直接面向过程写死代码，后期有时间在优化
    apk_smali = apk_file_path + "/smali"
    c2 = apk_file_path + "/smali_classes2"
    c3 = apk_file_path + "/smali_classes3"
    c4 = apk_file_path + "/smali_classes4"
    c5 = apk_file_path + "/smali_classes5"
    if (os.path.exists(c2)):
        apk_smali = c2
    if (os.path.exists(c3)):
        apk_smali = c3
    if (os.path.exists(c4)):
        apk_smali = c4
    if (os.path.exists(c5)):
        apk_smali = c5

    print("[inject] 查找apk里samli路径为: " + apk_smali)

    result = os.system("cp -r " + src_smali + " " + apk_smali)
    tool.check_command(result)
    print("[inject] 拷贝注入的smali到目标apk里面")


# 修改application,里面调用注入的方法
def modify_application_class(application_path):
    print("[inject] Application准备添加自定义方法调用")
    injectResult = False
    file_data = ""
    # 查找onCreate方法
    with open(application_path, "r") as f:
        flag = False
        count = 0
        for line in f:
            if ".method public onCreate()V" in line:
                print("[inject] Application 定位到onCreate方法")
                flag = True
                injectResult = True
            if flag:
                count += 1
            if count >= 6:
                inject_str = "    invoke-static {p0}, Lcom/playin/hook/PlayInject;->init(Landroid/content/Context;)V"
                file_data += "\n" + inject_str + "\n"
                flag = False
                count = 0
            file_data += line
    f.close()
    if (injectResult):
        with open(application_path, "w") as f:
            f.write(file_data)
        f.close()

    file_data = ""
    if (injectResult == False):
        # 查找attachBaseContext方法
        with open(application_path, "r") as f:
            flag = False
            count = 0
            for line in f:
                if ".method protected attachBaseContext(Landroid/content/Context;)V" in line:
                    print("[inject] Application 定位到attachBaseContext方法")
                    flag = True
                    injectResult = True
                if flag:
                    count += 1
                if count >= 7:
                    inject_str = "    invoke-static {p0}, Lcom/playin/hook/PlayInject;->init(Landroid/content/Context;)V"
                    file_data += "\n" + inject_str + "\n"
                    flag = False
                    count = 0
                file_data += line
        f.close()
        if (injectResult):
            with open(application_path, "w") as f:
                f.write(file_data)
            f.close()

    if (injectResult):
        print("[inject] Application 注入方法成功")
    else:
        print("[inject] Application 注入方法失败")
        tool.check_command(-1)

# 修改mainactivity,里面调用注入的方法
def modify_main_activity_class(main_activity_path):
    print("[inject] main_activity_path准备添加自定义方法调用")
    injectResult = False
    file_data = ""
    # 查找onCreate方法
    with open(main_activity_path, "r") as f:
        flag = False
        count = 0
        for line in f:
            if ".method protected onCreate(Landroid/os/Bundle;)V" in line:
                print("[inject] MainActivity 定位到onCreate方法")
                flag = True
                injectResult = True
            if flag:
                count += 1
            if count >= 9:
                inject_str = "    invoke-static {p0}, Lcom/playin/hook/PlayInject;->init(Landroid/content/Context;)V"
                file_data += "\n" + inject_str + "\n"
                flag = False
                count = 0
            file_data += line
    f.close()
    if (injectResult):
        with open(main_activity_path, "w") as f:
            f.write(file_data)
        f.close()

    if (injectResult):
        print("[inject] MainActivity 注入方法成功")
    else:
        print("[inject] MainActivity 注入方法失败")
        tool.check_command(-1)

def modify_hook_java(apk_file_path):
    print("[inject] 修改hook java音频代码 " + apk_file_path)
    # result = os.system("sed -i '' 's/Landroid\/media\/AudioTrack;/Lcom\/playin\/hook\/HookJava;/g' `grep 'Landroid/media/AudioTrack;' -rl " + apk_file_path + " --include 'FMODAudioDevice.smali'`")
    result = os.system("sed -i '' 's/Landroid\/media\/AudioTrack;/Lcom\/playin\/hook\/HookJava;/g' `grep 'Landroid/media/AudioTrack;' -rl " + apk_file_path + " --exclude 'HookJava.smali'`")
    tool.check_command(result)


def process(apk_file_path):
    src_java_path()
    javac_class()
    dex_class()
    smali_dex()
    # copy_libs_apk(apk_file_path)
    copy_smali_apk(apk_file_path)
    # modify_hook_java(apk_file_path)

    # application 注入方法调用
    # app_package_name = tool.get_app_class(apk_file_path)
    # if app_package_name != None:
    #     print("[inject] 获取到Application对应的包名 " + app_package_name)
    #     application_path = tool.find_application_path(apk_file_path, app_package_name)
    #     if application_path:
    #         modify_application_class(application_path)
    # else:
    #     print("[inject] 获取到Application对应的包名失败 ")

    # mainActivity里面注入入口方法调用
    main_activity_class = tool.find_main_activity_class(apk_file_path)
    main_activity_path = tool.find_main_activity_path(apk_file_path, main_activity_class)
    modify_main_activity_class(main_activity_path)
    # application 添加权限
    tool.inject_application_permission(apk_file_path)




# application没有找到入口，就修改UnityPlayerActivity
# def modify_act_class(apk_file_path):
#     command_str = "find %s %s" % (apk_file_path, "UnityPlayerActivity.smali")
#     print(command_str)
#     dest_path = tool.exec_cmd(command_str)
#     if os.path.exists(dest_path):
#         print("[inject] UnityPlayerActivity.smali路径为 " + dest_path)
#     else:
#         print("[inject] UnityPlayerActivity.smali没有找到 ")



def main():
    tool.create_temp_file()
    apks_path = tool.apk_src_path()
    if len(apks_path) == 0:
        print("[inject] 请在根目录放入需要注入的apk文件")
    elif len(apks_path) != 1:
        print("[inject] 根目录只能包含一个apk文件")
    else:
        apk_file_path = tool.apktool_d(apks_path[0])

        main_activity_class = tool.find_main_activity_class(apk_file_path)
        main_activity_path = tool.find_main_activity_path(apk_file_path, main_activity_class)
        modify_main_activity_class(main_activity_path)
        tool.inject_application_permission(apk_file_path)

        # hook_audio(apk_file_path)
        #
        # new_apk_path = tool.apktool_b(apk_file_path)
        # new_apk_path = tool.sign_apk(new_apk_path)
        # print("[inject] 签名成功，路径为: " + new_apk_path)


# main()