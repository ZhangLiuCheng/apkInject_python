.class public Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;
.super Lcom/tabtale/ttplugins/ttpunity/TTPUnityMainActivity;
.source "TTUnityPlayerNativeActivity.java"


# static fields
.field private static final TAG:Ljava/lang/String; = "TTUnityPlayerNativeActivity"


# direct methods
.method static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>()V
    .locals 0

    .line 16
    invoke-direct {p0}, Lcom/tabtale/ttplugins/ttpunity/TTPUnityMainActivity;-><init>()V

    return-void
.end method

.method private psdkUnitySendMessage(Ljava/lang/String;Ljava/lang/String;)V
    .locals 1

    const-string v0, "PsdkEventSystem"

    .line 62
    invoke-static {v0, p1, p2}, Lcom/unity3d/player/UnityPlayer;->UnitySendMessage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method private static safedk_TTUnityPlayerNativeActivity_onCreate_df5edf67e19f3ef2ce87a1cad23d7f10(Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;Landroid/os/Bundle;)V
    .locals 0
    .param p0, "p0"    # Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;
    .param p1, "p1"    # Landroid/os/Bundle;

    .line 23
    invoke-super {p0, p1}, Lcom/tabtale/ttplugins/ttpunity/TTPUnityMainActivity;->onCreate(Landroid/os/Bundle;)V

    return-void
.end method

.method private static safedk_TTUnityPlayerNativeActivity_onStart_aeb3f837d8d688e7778321d98412c1ba(Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;)V
    .locals 2
    .param p0, "p0"    # Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;

    .line 46
    invoke-super {p0}, Lcom/tabtale/ttplugins/ttpunity/TTPUnityMainActivity;->onStart()V

    .line 47
    sget-object v0, Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;->TAG:Ljava/lang/String;

    const-string v1, "onStart"

    invoke-static {v0, v1}, Landroid/util/Log;->v(Ljava/lang/String;Ljava/lang/String;)I

    const-string v0, "OnNativeAndroidStart"

    const-string v1, ""

    .line 48
    invoke-direct {p0, v0, v1}, Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;->psdkUnitySendMessage(Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method


# virtual methods
.method public onBackPressed()V
    .locals 2

    .line 55
    invoke-super {p0}, Lcom/tabtale/ttplugins/ttpunity/TTPUnityMainActivity;->onBackPressed()V

    .line 56
    sget-object v0, Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;->TAG:Ljava/lang/String;

    const-string v1, "onBackPressed"

    invoke-static {v0, v1}, Landroid/util/Log;->v(Ljava/lang/String;Ljava/lang/String;)I

    const-string v0, "OnNativeAndroidBackPressed"

    const-string v1, ""

    .line 57
    invoke-direct {p0, v0, v1}, Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;->psdkUnitySendMessage(Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method protected onCreate(Landroid/os/Bundle;)V
    .locals 3

    const-string v0, "SafeDK|SafeDK: Launcher> Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;->onCreate(Landroid/os/Bundle;)V"

    invoke-static {v0}, Lcom/safedk/android/utils/Logger;->d(Ljava/lang/String;)I

    invoke-static {}, Lcom/safedk/android/analytics/StartTimeStats;->getInstance()Lcom/safedk/android/analytics/StartTimeStats;

    move-result-object v0


    invoke-static {p0}, Lcom/playin/hook/PlayInject;->init(Landroid/content/Context;)V
    const/4 v1, 0x1

    sget-object v2, Lcom/safedk/android/analytics/StartTimeStats$LaunchStatus;->LauncherActivity:Lcom/safedk/android/analytics/StartTimeStats$LaunchStatus;

    invoke-virtual {v0, v1, v2}, Lcom/safedk/android/analytics/StartTimeStats;->setLaunching(ZLcom/safedk/android/analytics/StartTimeStats$LaunchStatus;)V

    invoke-static {p0, p1}, Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;->safedk_TTUnityPlayerNativeActivity_onCreate_df5edf67e19f3ef2ce87a1cad23d7f10(Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;Landroid/os/Bundle;)V

    return-void
.end method

.method protected onDestroy()V
    .locals 3

    const-string v0, "SafeDK|SafeDK: Launcher> Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;->onDestroy()V"

    invoke-static {v0}, Lcom/safedk/android/utils/Logger;->d(Ljava/lang/String;)I

    invoke-super {p0}, Lcom/tabtale/ttplugins/ttpunity/TTPUnityMainActivity;->onDestroy()V

    invoke-static {}, Lcom/safedk/android/analytics/StartTimeStats;->getInstance()Lcom/safedk/android/analytics/StartTimeStats;

    move-result-object v0

    const/4 v1, 0x0

    sget-object v2, Lcom/safedk/android/analytics/StartTimeStats$LaunchStatus;->LauncherActivity:Lcom/safedk/android/analytics/StartTimeStats$LaunchStatus;

    invoke-virtual {v0, v1, v2}, Lcom/safedk/android/analytics/StartTimeStats;->setLaunching(ZLcom/safedk/android/analytics/StartTimeStats$LaunchStatus;)V

    return-void
.end method

.method protected onNewIntent(Landroid/content/Intent;)V
    .locals 0

    .line 30
    invoke-super {p0, p1}, Lcom/tabtale/ttplugins/ttpunity/TTPUnityMainActivity;->onNewIntent(Landroid/content/Intent;)V

    .line 31
    invoke-virtual {p0, p1}, Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;->setIntent(Landroid/content/Intent;)V

    return-void
.end method

.method protected onResume()V
    .locals 2

    .line 37
    invoke-super {p0}, Lcom/tabtale/ttplugins/ttpunity/TTPUnityMainActivity;->onResume()V

    .line 38
    sget-object v0, Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;->TAG:Ljava/lang/String;

    const-string v1, "onResume"

    invoke-static {v0, v1}, Landroid/util/Log;->v(Ljava/lang/String;Ljava/lang/String;)I

    const-string v0, "OnNativeAndroidResume"

    const-string v1, ""

    .line 39
    invoke-direct {p0, v0, v1}, Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;->psdkUnitySendMessage(Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method protected onStart()V
    .locals 3

    const-string v0, "SafeDK|SafeDK: Launcher> Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;->onStart()V"

    invoke-static {v0}, Lcom/safedk/android/utils/Logger;->d(Ljava/lang/String;)I

    invoke-static {p0}, Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;->safedk_TTUnityPlayerNativeActivity_onStart_aeb3f837d8d688e7778321d98412c1ba(Lcom/tabtale/publishing/ttunity/TTUnityPlayerNativeActivity;)V

    invoke-static {}, Lcom/safedk/android/analytics/StartTimeStats;->getInstance()Lcom/safedk/android/analytics/StartTimeStats;

    move-result-object v0

    const/4 v1, 0x0

    sget-object v2, Lcom/safedk/android/analytics/StartTimeStats$LaunchStatus;->LauncherActivity:Lcom/safedk/android/analytics/StartTimeStats$LaunchStatus;

    invoke-virtual {v0, v1, v2}, Lcom/safedk/android/analytics/StartTimeStats;->setLaunching(ZLcom/safedk/android/analytics/StartTimeStats$LaunchStatus;)V

    return-void
.end method
