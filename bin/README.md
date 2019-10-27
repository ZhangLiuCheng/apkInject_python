### 一. 运行环境配置

​    **java**, **apktool**, **dx**, **python**

### 二. 使用文档

1. 将需要注入的代码拷贝到apkInject/src目录下面，在AudioHook.java init()里面调用你自己的方法
2. 将源apk拷贝的apkInject/ 目录下面
3. 进入apkInject/bin目录里面执行python inject.py
4. 注入完成，新apk将在temp/目录下面

### 三. 声音获取步骤

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

### 四. 去广告步骤

​	目前去除的已知广告有IronSource, Admob, facebook, MoPub, AppLovinSdk, Vungle, Amazon, Chartboost后期在慢慢添加广告。

#### 	1.IronSource

​		IronSource.init(this, appKey, IronSource.AD_UNIT.REWARDED_VIDEO); 替换了里面初始化方法

> #### https://developers.ironsrc.com/ironsource-mobile/android/android-sdk/#step-5

#### 	2.Facebook

​		FacebookSdk.fullyInitialize(); 替换了里面初始化方法

> https://developers.facebook.com/docs/app-events/getting-started-app-events-android

#### 	3.MoPub

​		MoPub.initializeSdk(this, sdkConfiguration, initSdkListener());

> https://developers.mopub.com/publishers/android/get-started/

#### 	4.AppLovin

​		AppLovinSdk.initializeSdk(context);

​		AppLovinFacade.InitializeSdk

​		AppLovinFacade.LoadNextAd

​		AppLovinFacade.ShowAd	

​		AppLovinFacade.ShowIncentInterstitial		

> https://dash.applovin.com/docs/integration?signed_up=1#androidIntegration

#### 	5.Vungle

​		Vungle.init("YOUR_VUNGLE_APP_ID", getApplicationContext(), callback)

> https://support.vungle.com/hc/zh-cn/articles/360033384691#注意-0-4

#### 	6.Admob

​		MobileAds.initialize(this, listener);

> https://developers.google.com/admob/android/quick-start

#### 	7.Amazon

​		AdRegistration.setAppKey("0123456789ABCDEF0123456789ABCDEF");

> https://developer.amazon.com/zh/docs/mobile-ads/mb-quick-start.html

#### 	8.Chartboost

​		Chartboost.startWithAppId(this, appId, appSignature);

> https://answers.chartboost.com/en-us/child_article/android

### 	9.mintegral

​		MIntegralSDK.init(map, this);

> http://cdn-adn.rayjump.com/cdn-adn/v2/markdown_v2/index.html?file=sdk-m_sdk-android&lang=en

​		修改AndroidManifest里面MTGRewardVideoActivity，禁止奖励视频弹出

### 	10.UnityAds

​		UnityAds.initialize(); UnityAds.show(); 



### 11.GooglePlayService 弹窗

```java
GoogleApiAvailability.isGooglePlayServicesAvailable
```

> https://developers.google.com/android/guides/setup



###  个别游戏手动修改

###  	1.Origame

 		PromotionActivity  onCreate添加

```
invoke-virtual {p0}, Lcom/ketchapp/promotion/PromotionActivity;->finish()V
return-void
```

​		

### 2.Fastlane.apk

​		UnityPlayerActivity onCreate添加

```java
invoke-static {p0}, Lcom/playin/hook/PlayInject;->init(Landroid/app/Activity;)V
```