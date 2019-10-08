#!/usr/bin/python
# coding: UTF-8
import os, time


def check_command(result):
    if result != 0:
        # os.system("pause")
        print("!!!!!!!!!!!!   命令执行异常  !!!!!!!!!!!!!")
        exit()
        # os.system("exit 0")


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


def insert_log(apk_file_path, file_name, message):

    log_str = '''\n\n.method public static playInLog()V
    .registers 2

    .prologue
    .line 51
    const-string v0, "Hook_AD"

    const-string v1, "%s"

    invoke-static {v0, v1}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 52
    return-void
.end method''' % (message)

    command_str = "find %s %s" % (apk_file_path, file_name)
    print(command_str)
    dest_path = exec_cmd(command_str)
    if (os.path.exists(dest_path) == True):
        fp = open(dest_path, 'a')
        fp.write(log_str)
        fp.close()
        print("[inject_ad] 插入自定义打印语句  " + dest_path)
        return True
    else:
        return False


# Unity 广告拦截
def ad_unity(apk_file_path):
    file_name = 'UnityAds.smali'
    find_command_str = " -name " + file_name
    insert_result = insert_log(apk_file_path, find_command_str, "UnityAds   ---->  广告已被拦截")
    print("[inject_ad] UnityAds广告拦截" + apk_file_path)
    print_str = "\\\n\\\t" + "invoke-static {}, Lcom\/unity3d\/ads\/UnityAds;->playInLog()V" + "\\\n\\\n\\\treturn-void\\\n"

    if (insert_result == True):
        init_str1 = ".method public static initialize(Landroid\/app\/Activity;Ljava\/lang\/String;Lcom\/unity3d\/ads\/IUnityAdsListener;Z)V"
        log_str1 = init_str1 + print_str
        command_str1 = "sed -i '' 's/%s/%s/g' `grep '%s' -rl %s --include '%s'`" % (init_str1, log_str1, init_str1, apk_file_path, file_name)
        result1 = os.system(command_str1)
        check_command(result1)

        init_str2 = ".method public static show(Landroid\/app\/Activity;)V"
        log_str2 = init_str2 + print_str
        command_str2 = "sed -i '' 's/%s/%s/g' `grep '%s' -rl %s --include '%s'`" % (init_str2, log_str2, init_str2, apk_file_path, file_name)
        result2 = os.system(command_str2)
        check_command(result2)

        init_str3 = ".method public static show(Landroid\/app\/Activity;Ljava\/lang\/String;)V"
        log_str3 = init_str3 + print_str
        command_str3 = "sed -i '' 's/%s/%s/g' `grep '%s' -rl %s --include '%s'`" % (init_str3, log_str3, init_str3, apk_file_path, file_name)
        result3 = os.system(command_str3)
        check_command(result3)

        print("[inject_ad] UnityAds初始化方法替换成打印方法")
    else:
        print("[inject_ad] UnityAds初始化方法替换失败, UnityAds.smail文件不存在")


# IronSource 广告拦截
def ad_ironsource(apk_file_path):
    file_name = "IronSource.smali"
    find_command_str = " -name " + file_name
    init_str = ".method public static varargs init(Landroid\/app\/Activity;Ljava\/lang\/String;\[Lcom\/ironsource\/mediationsdk\/IronSource$AD_UNIT;)V"
    log_str = init_str + "\\\n\\\t" + "invoke-static {}, Lcom\/ironsource\/mediationsdk\/IronSource;->playInLog()V" + "\\\n\\\n\\\treturn-void\\\n"

    insert_result = insert_log(apk_file_path, find_command_str, "IronSource   ---->  广告已被拦截")
    if (insert_result == True):
        print("[inject_ad] IronSource广告拦截" + apk_file_path)
        command_str = "sed -i '' 's/%s/%s/g' `grep '%s' -rl %s --include '%s'`" % (init_str, log_str, init_str, apk_file_path, file_name)
        result = os.system(command_str)
        check_command(result)
        print("[inject_ad] IronSource初始化方法替换成打印方法")
    else:
        print("[inject_ad] IronSource.smail文件不存在")


# Facebook 广告拦截
def ad_facebook(apk_file_path):
    file_name = "FacebookSdk.smali"
    find_command_str = " -name " + file_name
    init_str = ".method public static declared-synchronized sdkInitialize(Landroid\/content\/Context;Lcom\/facebook\/FacebookSdk$InitializeCallback;)V"
    log_str = init_str + "\\\n\\\t" + "invoke-static {}, Lcom\/facebook\/FacebookSdk;->playInLog()V" + "\\\n\\\n\\\treturn-void\\\n"


    insert_result = insert_log(apk_file_path, find_command_str, "FacebookSdk   ---->  广告已被拦截")
    if (insert_result == True):
        print("[inject_ad] FacebookSdk广告拦截 " + apk_file_path)
        command_str = "sed -i '' 's/%s/%s/g' `grep '%s' -rl %s --include '%s'`" % (init_str, log_str, init_str, apk_file_path, file_name)
        result = os.system(command_str)
        check_command(result)
        print("[inject_ad] FacebookSdk初始化方法替换成打印方法")
    else:
        print("[inject_ad] FacebookSdk.smail文件不存在")


# MoPub 广告拦截
def ad_mopub(apk_file_path):
    file_name = "MoPub.smali"
    find_command_str = " -name " + file_name
    init_str = "invoke-virtual {p2, p0, v0, v1, p1}, Lcom\/mopub\/common\/AdapterConfigurationManager;->initialize(Landroid\/content\/Context;Ljava\/util\/Set;Ljava\/util\/Map;Ljava\/util\/Map;)V"
    log_str = "invoke-static {}, Lcom\/mopub\/common\/MoPub;->playInLog()V"

    insert_result = insert_log(apk_file_path, find_command_str, "MoPub   ---->  广告已被拦截")
    if (insert_result == True):
        print("[inject_ad] MoPub广告拦截" + apk_file_path)
        command_str = "sed -i '' 's/%s/%s/g' `grep '%s' -rl %s --include '%s'`" % (init_str, log_str, init_str, apk_file_path, file_name)
        result = os.system(command_str)
        check_command(result)
        print("[inject_ad] MoPub初始化方法替换成打印方法")
    else:
        print("[inject_ad] MoPub.smail文件不存在")


# Vungle 广告拦截
def ad_vungle(apk_file_path):
    file_name = "Vungle.smali"
    find_command_str = " -name " + file_name
    # init_str = ".method public static init(Ljava\/lang\/String;Landroid\/content\/Context;Lcom\/vungle\/warren\/InitCallback;Lcom\/vungle\/warren\/VungleSettings;)V"
    init_str = ".method public static init(Ljava\/lang\/String;Landroid\/content\/Context;Lcom\/vungle\/warren\/InitCallback;Lcom\/vungle\/warren\/PublisherDirectDownload;)V"
    log_str = init_str + "\\\n\\\t" + "invoke-static {}, Lcom\/vungle\/warren\/Vungle;->playInLog()V" + "\\\n\\\n\\\treturn-void\\\n"

    insert_result = insert_log(apk_file_path, find_command_str, "Vungle   ---->  广告已被拦截")
    if (insert_result == True):
        print("[inject_ad] Vungle广告拦截" + apk_file_path)
        command_str = "sed -i '' 's/%s/%s/g' `grep '%s' -rl %s --include '%s'`" % (init_str, log_str, init_str, apk_file_path, file_name)
        result = os.system(command_str)
        check_command(result)
        print("[inject_ad] Vungle初始化方法替换成打印方法")
    else:
        print("[inject_ad] Vungle.smail文件不存在")


# AppLovin 广告拦截
def ad_appLovin(apk_file_path):
    file_name = "AppLovinSdk.smali"
    find_command_str = " -name " + file_name
    init_str = ".method public static initializeSdk(Landroid\/content\/Context;Lcom\/applovin\/sdk\/AppLovinSdk$SdkInitializationListener;)V"
    log_str = init_str + "\\\n\\\t" + "invoke-static {}, Lcom\/applovin\/sdk\/AppLovinSdk;->playInLog()V" + "\\\n\\\n\\\treturn-void\\\n"

    insert_result = insert_log(apk_file_path, find_command_str, "Vungle   ---->  广告已被拦截")
    if (insert_result == True):
        print("[inject_ad] AppLovin广告拦截" + apk_file_path)
        command_str = "sed -i '' 's/%s/%s/g' `grep '%s' -rl %s --include '%s'`" % (init_str, log_str, init_str, apk_file_path, file_name)
        result = os.system(command_str)
        check_command(result)
        print("[inject_ad] AppLovin初始化方法替换成打印方法")
    else:
        print("[inject_ad] AppLovinSdk.smail文件不存在")


# Admob 广告拦截
def ad_admob(apk_file_path):
    file_name = "MobileAds.smali"
    find_command_str = " -name " + file_name
    init_str = ".method public static initialize(Landroid\/content\/Context;Ljava\/lang\/String;Lcom\/google\/android\/gms\/ads\/MobileAds$Settings;)V"
    log_str = init_str + "\\\n\\\t" + "invoke-static {}, Lcom\/google\/android\/gms\/ads\/MobileAds;->playInLog()V" + "\\\n\\\n\\\treturn-void\\\n"

    insert_result = insert_log(apk_file_path, find_command_str, "Admob   ---->  广告已被拦截")
    if (insert_result == True):
        print("[inject_ad] Admob广告拦截" + apk_file_path)
        command_str = "sed -i '' 's/%s/%s/g' `grep '%s' -rl %s --include '%s'`" % (init_str, log_str, init_str, apk_file_path, file_name)
        result = os.system(command_str)
        check_command(result)
        print("[inject_ad] Admob初始化方法替换成打印方法")
    else:
        print("[inject_ad] MobileAds.smail文件不存在")


# Amazon 广告拦截
def ad_amazon(apk_file_path):
    file_name = "AdRegistration.smali"
    find_command_str = " -name " + file_name
    init_str = ".method public static final setAppKey(Ljava\/lang\/String;)V"
    log_str = init_str + "\\\n\\\t" + "invoke-static {}, Lcom\/amazon\/device\/ads\/AdRegistration;->playInLog()V" + "\\\n\\\n\\\treturn-void\\\n"

    insert_result = insert_log(apk_file_path,find_command_str , "Amazon   ---->  广告已被拦截")
    if (insert_result == True):
        print("[inject_ad] Amazon广告拦截" + apk_file_path)
        command_str = "sed -i '' 's/%s/%s/g' `grep '%s' -rl %s --include '%s'`" % (init_str, log_str, init_str, apk_file_path, file_name)
        result = os.system(command_str)
        check_command(result)
        print("[inject_ad] Amazon初始化方法替换成打印方法")
    else:
        print("[inject_ad] AdRegistration.smail文件不存在")


# Chartboost 广告拦截
def ad_chartboost(apk_file_path):
    file_name = "Chartboost.smali"
    find_command_str = " -name " + file_name
    init_str = ".method public static startWithAppId(Landroid\/app\/Activity;Ljava\/lang\/String;Ljava\/lang\/String;)V"
    log_str = init_str + "\\\n\\\t" + "invoke-static {}, Lcom\/chartboost\/sdk\/Chartboost;->playInLog()V" + "\\\n\\\n\\\treturn-void\\\n"

    insert_result = insert_log(apk_file_path, find_command_str, "Chartboost   ---->  广告已被拦截")
    if (insert_result == True):
        print("[inject_ad] Chartboost广告拦截" + apk_file_path)
        command_str = "sed -i '' 's/%s/%s/g' `grep '%s' -rl %s --include '%s'`" % (init_str, log_str, init_str, apk_file_path, file_name)
        result = os.system(command_str)
        check_command(result)
        print("[inject_ad] Chartboost初始化方法替换成打印方法")
    else:
        print("[inject_ad] Chartboost.smail文件不存在")


# Mintegral 广告拦截
def ad_mintegra(apk_file_path):
    #修改Manifest
    manifest = apk_file_path + "/AndroidManifest.xml"
    command_str = "sed -i '' 's/MTGRewardVideoActivity/MTGRewardVideoActivity_temp/g' " + manifest
    result = os.system(command_str)
    check_command(result)


    file_name = 'a.smali'
    find_command_str = ' -path "*/com/mintegral/msdk/system*"  -name ' + file_name

    # 方法1
    # init_str = "invoke-virtual {v0, v1, p1}, Lcom\/mintegral\/msdk\/base\/controller\/b;->a(Ljava\/util\/Map;Landroid\/content\/Context;)V"

    # 方法2
    init_str = "invoke-virtual {v0, v1, v2}, Lcom\/mintegral\/msdk\/base\/controller\/b;->a(Ljava\/util\/Map;Landroid\/content\/Context;)V"

    log_str = init_str + "\\\n\\\t" + "invoke-static {}, Lcom\/mintegral\/msdk\/system\/a;->playInLog()V" + "\\\n"

    insert_result = insert_log(apk_file_path, find_command_str, "Mintegral   ---->  广告已被拦截")
    if (insert_result == True):
        print("[inject_ad] Mintegral广告拦截" + apk_file_path)
        command_str = "sed -i '' 's/%s/%s/g' `grep '%s' -rl %s --include '%s'`" % (init_str, log_str, init_str, apk_file_path, file_name)
        result = os.system(command_str)
        check_command(result)
        print("[inject_ad] Mintegral初始化方法替换成打印方法")
    else:
        print("[inject_ad] Mintegral初始化方法替换失败, a.smail文件不存在")


def main():
    create_temp_file()
    apks_path = apk_src_path()

    if len(apks_path) == 0:
        print("[inject] 请在根目录放入需要注入的apk文件")
    elif len(apks_path) != 1:
        print("[inject] 根目录只能包含一个apk文件")
    else:
        apk_file_path = apktool_d(apks_path[0])

        ad_unity(apk_file_path)
        ad_ironsource(apk_file_path)
        ad_facebook(apk_file_path)
        ad_mopub(apk_file_path)
        ad_vungle(apk_file_path)
        ad_appLovin(apk_file_path)
        ad_admob(apk_file_path)
        ad_amazon(apk_file_path)
        ad_chartboost(apk_file_path)
        ad_mintegra(apk_file_path)

        new_apk_path = apktool_b(apk_file_path)
        new_apk_path = sign_apk(new_apk_path)
        print("[inject] 签名成功，路径为: " + new_apk_path)


main()

# (1)创五权分立原则，谋求“万能政府”; (2)倡导地方自治，提出均权制主张; (3)重视人才的培养与合理使用，强调人尽其才
#
# (1)行政组织应以经济职能为中心。 (2)行政组织必须进行改革。(改革是核心) (3)行政组织必须克服官僚主义，提高工作效率。 (4)行政组织必须加强法制建设。
#
# 邓小平关于行政组织必须加强法制建设的思想的内容:
# (1)必须加强组织制度建设;(2)重视组织机构的编制立法; (3)主张建立严格的工作责任制; (4)强调必须建立科学的干部人事管理制度。