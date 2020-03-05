#!/usr/bin/python
# coding: UTF-8

import advert
import code
import tool


def main():
    tool.create_temp_file()
    apks_path = tool.apk_src_path()
    if len(apks_path) == 0:
        print("[inject] 请在根目录放入需要注入的apk文件")
    elif len(apks_path) != 1:
        print("[inject] 根目录只能包含一个apk文件")
    else:
        apk_file_path = tool.apktool_d(apks_path[0])

        #AndroidMainfest.xml 手动更添加下面代码
        #<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
        #<uses-permission android:name="android.permission.CALL_PHONE"/>

        # 自动注入代码有问题，手动添加下面代码
        #invoke-static {p0}, Lcom/playin/hook/PlayInject;->init(Landroid/content/Context;)V

        advert.hook(apk_file_path)
        code.process(apk_file_path)

        new_apk_path = tool.apktool_b(apk_file_path)
        new_apk_path = tool.sign_apk(new_apk_path)
        print("[inject] 签名成功，路径为: " + new_apk_path)

        #pm clear com.habby.archero
        #pm grant com.habby.archero android.permission.READ_EXTERNAL_STORAGE

        #cat /data/system/packages.xml | grep grep com.habby.archero

        #iptables -I OUTPUT -m owner --uid-owner 10141 -p tcp -j DROP
        #iptables -I OUTPUT -m owner --uid-owner 10141 -p udp -j DROP

        #pm clear com.joypac.jpescape
        #pm grant com.joypac.jpescape android.permission.READ_EXTERNAL_STORAGE
        #pm grant com.joypac.jpescape android.permission.CALL_PHONE


main()
