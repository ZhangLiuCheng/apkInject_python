#### 一. 广告平台

目前去除的已知广告有IronSource, Admob, facebook, MoPub, AppLovinSdk, Vungle, amazon,后期在慢慢添加广告。



#### 二. 去除步骤

#### 1.IronSource

​		IronSource.init(this, appKey, IronSource.AD_UNIT.REWARDED_VIDEO); 替换了里面初始化方法

> #### https://developers.ironsrc.com/ironsource-mobile/android/android-sdk/#step-5
>
> 

#### 2.Facebook

​		FacebookSdk.fullyInitialize(); 替换了里面初始化方法

> https://developers.facebook.com/docs/app-events/getting-started-app-events-android

​		

#### 3.MoPub

​		MoPub.initializeSdk(this, sdkConfiguration, initSdkListener());

> https://developers.mopub.com/publishers/android/get-started/



#### 4.AppLovin

​		AppLovinSdk.initializeSdk(context);

> https://dash.applovin.com/docs/integration?signed_up=1#androidIntegration



#### 5.Vungle

​		Vungle.init("YOUR_VUNGLE_APP_ID", getApplicationContext(), callback)

> https://support.vungle.com/hc/zh-cn/articles/360033384691#注意-0-4



#### 6.Admob

​		MobileAds.initialize(this, listener);

> https://developers.google.com/admob/android/quick-start



#### 7.Amazon

​		AdRegistration.setAppKey("0123456789ABCDEF0123456789ABCDEF");

> https://developer.amazon.com/zh/docs/mobile-ads/mb-quick-start.html



#### 8.Chartboost

​		Chartboost.startWithAppId(this, appId, appSignature);

> https://answers.chartboost.com/en-us/child_article/android



### 9.mintegral

​		MIntegralSDK.init(map, this);

> http://cdn-adn.rayjump.com/cdn-adn/v2/markdown_v2/index.html?file=sdk-m_sdk-android&lang=en