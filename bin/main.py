#!/usr/bin/python
# coding: UTF-8
import advert
import audio
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

        # advert.hook_advert(apk_file_path)
        # audio.hook_audio(apk_file_path)

        new_apk_path = tool.apktool_b(apk_file_path)
        new_apk_path = tool.sign_apk(new_apk_path)
        print("[inject] 签名成功，路径为: " + new_apk_path)


main()
