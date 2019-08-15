
一. 环境配置
    java, apktool, dx, python
    
二. 注入流程

    # 查找所有java文件
    1. find . -name "*.java" > sources.txt

    # java转class
    2. mkdir outputClass
    3. javac -classpath android.jar @sources.txt -d outputClass/

    # class转dex
    4. dx --dex --output=./apkInject.dex outputClass

    # dex转smail
    5. java -jar baksmali.jar d apkInject.dex -o outputSmail/
    
    # 反编译apk
    6. apktool d xxx.apk

    # 拷贝outputSmail到反编译后的文件里，并且带哦用
    7.
    
    # 打包apk 
    8. apktool b xxx
    
    # 签名
    9. jarsigner -verbose -keystore playin.jks -signedjar sign.apk playInDemo.apk playin -storepass playin

三. 使用说明


