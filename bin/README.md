#### 一. 运行环境配置

​    **java**, **apktool**, **dx**, **python**

#### 二. 注入流程说明

1. 查找所有java文件

   `find . -name "*.java" > sources.txt`

2. java转class

   `javac -classpath android.jar @sources.txt -d outputClass/`

3. class转dex

   `dx --dex --output=./apkInject.dex outputClass`

4. dex转smail

   `java -jar baksmali.jar d apkInject.dex -o outputSmail/`

5. 反编译apk

   `apktool d xxx.apk`

6. 拷贝outputSmail到反编译后的文件里

   `cp -r src_smali apk_smali`

7. 源apk的smail里调用注入的方法

   通过解析AndroidManifest.xml获取到Application所在的文件，然后在OnCreate里面注入 

   ```
       invoke-static {p0}, Lcom/playin/hook/AudioHook;->init(Landroid/app/Application;)V
   ```

8. 打包apk 

   `apktool b xxx`

9. apk签名

   `jarsigner -verbose -keystore playin.jks -signedjar sign.apk playInDemo.apk playin -storepass playin`

#### 三. 使用文档

1. 将需要注入的代码拷贝到apkInject/src目录下面，在AudioHook.java init()里面调用你自己的方法

   <!--Test.java 和 util 是测试用的，可以删除-->

2. 将源apk拷贝的apkInject/ 目录下面

3. 进入apkInject/bin目录里面执行python inject.py

4. 注入完成，新apk将在temp/目录下面


