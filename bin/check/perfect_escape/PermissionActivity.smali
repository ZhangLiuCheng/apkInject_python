.class public Lcom/joypac/commonsdk/base/activity/PermissionActivity;
.super Landroid/app/Activity;
.source "PermissionActivity.java"

# interfaces
.implements Lcom/joypac/commonsdk/base/utils/permission/PermissionInterface;


# static fields
.field public static final SPLASH_IMG_CHINA:Ljava/lang/String; = "splash_img_china"

.field public static final SPLASH_IMG_INTERNATIONAL:Ljava/lang/String; = "splash_img_international"

.field public static final SPLASH_IMG_KEY:Ljava/lang/String; = "splash_img_key"

.field private static final SPLASH_JOYPAC_DELAY_TIME:J = 0x5dcL

.field public static final SPLASH_TYPE_JOYPAC:Ljava/lang/String; = "splash_joypac"

.field public static final SPLASH_TYPE_JOYPAC_CHINA:Ljava/lang/String; = "splash_joypac_china"

.field public static final SPLASH_TYPE_KEY:Ljava/lang/String; = "splash_type_key"

.field public static final SPLASH_TYPE_NONE:Ljava/lang/String; = "splash_none"

.field public static final SPLASH_TYPE_UPARPU:Ljava/lang/String; = "splash_uparpu"

.field private static final TAG:Ljava/lang/String; = "PermissionActivity"

.field private static final UPSDK_AD_ADAPTER_CLASS:Ljava/lang/String; = "com.joypac.upsdkplugin.UpSDKADAdapter"


# instance fields
.field private mHandler:Landroid/os/Handler;

.field private mPermissionHelper:Lcom/joypac/commonsdk/base/utils/permission/PermissionHelper;

.field private mSplashType:Ljava/lang/String;


# direct methods
.method public constructor <init>()V
    .locals 1

    .prologue
    .line 19
    invoke-direct {p0}, Landroid/app/Activity;-><init>()V

    .line 40
    new-instance v0, Landroid/os/Handler;

    invoke-direct {v0}, Landroid/os/Handler;-><init>()V

    iput-object v0, p0, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->mHandler:Landroid/os/Handler;

    return-void
.end method

.method static synthetic access$000(Lcom/joypac/commonsdk/base/activity/PermissionActivity;)V
    .locals 0
    .param p0, "x0"    # Lcom/joypac/commonsdk/base/activity/PermissionActivity;

    .prologue
    .line 19
    invoke-direct {p0}, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->goToMainActivity()V

    return-void
.end method

.method private goNextActivity()V
    .locals 8

    .prologue
    .line 98
    :try_start_0
    invoke-static {}, Lcom/joypac/commonsdk/base/utils/JoypacPropertiesUtils;->getInstance()Lcom/joypac/commonsdk/base/utils/JoypacPropertiesUtils;

    move-result-object v4

    const-string v5, "splash_type_key"

    invoke-virtual {v4, v5}, Lcom/joypac/commonsdk/base/utils/JoypacPropertiesUtils;->getBasicConfigValue(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    iput-object v4, p0, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->mSplashType:Ljava/lang/String;

    .line 99
    const-string v4, "PermissionActivity"

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "PermissionActivity goNextActivity splash type:"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    iget-object v6, p0, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->mSplashType:Ljava/lang/String;

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-static {v4, v5}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 101
    const-string v4, "splash_uparpu"

    iget-object v5, p0, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->mSplashType:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-eqz v4, :cond_1

    .line 103
    const-string v4, "PermissionActivity"

    const-string v5, "PermissionActivity \u663e\u793auparpu\u7684 splash"

    invoke-static {v4, v5}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 104
    const-string v4, "com.joypac.upsdkplugin.UpSDKADAdapter"

    invoke-static {v4}, Lcom/joypac/commonsdk/base/utils/CheckUtils;->checkClassExist(Ljava/lang/String;)Z

    move-result v0

    .line 105
    .local v0, "hasAddUparpu":Z
    if-eqz v0, :cond_0

    .line 106
    const-string v4, "PermissionActivity"

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "PermissionActivity \u6253\u5f00uparpu splash hasAddUparpu:"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-static {v4, v5}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 107
    invoke-direct {p0}, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->goToSplashActivity()V

    .line 144
    .end local v0    # "hasAddUparpu":Z
    :goto_0
    return-void

    .line 109
    .restart local v0    # "hasAddUparpu":Z
    :cond_0
    const-string v4, "PermissionActivity"

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "PermissionActivity \u6ca1\u6709\u96c6\u6210uparpu \u6253\u5f00mainactivity hasAddUparpu:"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-static {v4, v5}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 110
    invoke-direct {p0}, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->goToMainActivity()V
    :try_end_0
    .catch Ljava/lang/Throwable; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    .line 141
    .end local v0    # "hasAddUparpu":Z
    :catch_0
    move-exception v3

    .line 142
    .local v3, "t":Ljava/lang/Throwable;
    invoke-virtual {v3}, Ljava/lang/Throwable;->printStackTrace()V

    goto :goto_0

    .line 112
    .end local v3    # "t":Ljava/lang/Throwable;
    :cond_1
    :try_start_1
    const-string v4, "splash_joypac"

    iget-object v5, p0, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->mSplashType:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-eqz v4, :cond_3

    .line 114
    const-string v4, "PermissionActivity"

    const-string v5, "PermissionActivity \u663e\u793ajoypac splash"

    invoke-static {v4, v5}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 115
    sget v4, Lcom/joypac/commonsdk/R$layout;->activity_splash:I

    invoke-virtual {p0, v4}, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->setContentView(I)V

    .line 116
    sget v4, Lcom/joypac/commonsdk/R$id;->iv_splash:I

    invoke-virtual {p0, v4}, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->findViewById(I)Landroid/view/View;

    move-result-object v1

    check-cast v1, Landroid/widget/ImageView;

    .line 118
    .local v1, "ivSplash":Landroid/widget/ImageView;
    invoke-static {}, Lcom/joypac/commonsdk/base/utils/JoypacPropertiesUtils;->getInstance()Lcom/joypac/commonsdk/base/utils/JoypacPropertiesUtils;

    move-result-object v4

    const-string v5, "splash_img_key"

    invoke-virtual {v4, v5}, Lcom/joypac/commonsdk/base/utils/JoypacPropertiesUtils;->getBasicConfigValue(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    .line 119
    .local v2, "splashImg":Ljava/lang/String;
    const-string v4, "PermissionActivity"

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "PermissionActivity splashImg:"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-static {v4, v5}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 120
    const-string v4, "splash_img_china"

    invoke-virtual {v4, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-eqz v4, :cond_2

    .line 121
    const-string v4, "PermissionActivity"

    const-string v5, "PermissionActivity \u663e\u793a\u56fd\u5185\u7248\u56fe\u7247"

    invoke-static {v4, v5}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 122
    sget v4, Lcom/joypac/commonsdk/R$drawable;->joypac_splash_china:I

    invoke-virtual {v1, v4}, Landroid/widget/ImageView;->setImageResource(I)V

    .line 127
    :goto_1
    const/4 v4, 0x0

    invoke-virtual {v1, v4}, Landroid/widget/ImageView;->setVisibility(I)V

    .line 129
    iget-object v4, p0, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->mHandler:Landroid/os/Handler;

    new-instance v5, Lcom/joypac/commonsdk/base/activity/PermissionActivity$1;

    invoke-direct {v5, p0}, Lcom/joypac/commonsdk/base/activity/PermissionActivity$1;-><init>(Lcom/joypac/commonsdk/base/activity/PermissionActivity;)V

    const-wide/16 v6, 0x5dc

    invoke-virtual {v4, v5, v6, v7}, Landroid/os/Handler;->postDelayed(Ljava/lang/Runnable;J)Z

    goto/16 :goto_0

    .line 124
    :cond_2
    const-string v4, "PermissionActivity"

    const-string v5, "PermissionActivity \u663e\u793a\u56fd\u9645\u7248\u56fe\u7247"

    invoke-static {v4, v5}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 125
    sget v4, Lcom/joypac/commonsdk/R$drawable;->joypac_splash:I

    invoke-virtual {v1, v4}, Landroid/widget/ImageView;->setImageResource(I)V

    goto :goto_1

    .line 138
    .end local v1    # "ivSplash":Landroid/widget/ImageView;
    .end local v2    # "splashImg":Ljava/lang/String;
    :cond_3
    const-string v4, "PermissionActivity"

    const-string v5, "PermissionActivity \u9a6c\u4e0a\u6253\u5f00 goToMainActivity"

    invoke-static {v4, v5}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 139
    invoke-direct {p0}, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->goToMainActivity()V
    :try_end_1
    .catch Ljava/lang/Throwable; {:try_start_1 .. :try_end_1} :catch_0

    goto/16 :goto_0
.end method

.method private goToMainActivity()V
    .locals 6

    .prologue
    .line 159
    :try_start_0
    const-string v4, "PermissionActivity"

    const-string v5, "PermissionActivity goToMainActivity"

    invoke-static {v4, v5}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 161
    invoke-static {}, Lcom/joypac/commonsdk/base/utils/JoypacPropertiesUtils;->getInstance()Lcom/joypac/commonsdk/base/utils/JoypacPropertiesUtils;

    move-result-object v4

    sget-object v5, Lcom/joypac/commonsdk/base/BaseContansKey;->BASE_GAME_MAIN_ACTIVITY_CLASS_NAME:Ljava/lang/String;

    invoke-virtual {v4, v5}, Lcom/joypac/commonsdk/base/utils/JoypacPropertiesUtils;->getBasicConfigValue(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    .line 163
    .local v2, "mainClassName":Ljava/lang/String;
    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    .line 164
    .local v1, "mainClass":Ljava/lang/Class;
    new-instance v0, Landroid/content/Intent;

    invoke-direct {v0, p0, v1}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    .line 165
    .local v0, "intent":Landroid/content/Intent;
    invoke-virtual {p0, v0}, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->startActivity(Landroid/content/Intent;)V

    .line 166
    invoke-virtual {p0}, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->finish()V
    :try_end_0
    .catch Ljava/lang/Throwable; {:try_start_0 .. :try_end_0} :catch_0

    .line 170
    .end local v0    # "intent":Landroid/content/Intent;
    .end local v1    # "mainClass":Ljava/lang/Class;
    .end local v2    # "mainClassName":Ljava/lang/String;
    :goto_0
    return-void

    .line 167
    :catch_0
    move-exception v3

    .line 168
    .local v3, "t":Ljava/lang/Throwable;
    invoke-virtual {v3}, Ljava/lang/Throwable;->printStackTrace()V

    goto :goto_0
.end method

.method private goToSplashActivity()V
    .locals 3

    .prologue
    .line 148
    :try_start_0
    const-string v1, "PermissionActivity"

    const-string v2, "PermissionActivity goToSplashActivity"

    invoke-static {v1, v2}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 149
    new-instance v1, Landroid/content/Intent;

    const-class v2, Lcom/joypac/commonsdk/base/activity/SplashActivity;

    invoke-direct {v1, p0, v2}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    invoke-virtual {p0, v1}, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->startActivity(Landroid/content/Intent;)V

    .line 150
    invoke-virtual {p0}, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->finish()V
    :try_end_0
    .catch Ljava/lang/Throwable; {:try_start_0 .. :try_end_0} :catch_0

    .line 154
    :goto_0
    return-void

    .line 151
    :catch_0
    move-exception v0

    .line 152
    .local v0, "t":Ljava/lang/Throwable;
    invoke-virtual {v0}, Ljava/lang/Throwable;->printStackTrace()V

    goto :goto_0
.end method


# virtual methods
.method public getPermissions()[Ljava/lang/String;
    .locals 3

    .prologue
    .line 77
    const/4 v0, 0x2

    new-array v0, v0, [Ljava/lang/String;

    const/4 v1, 0x0

    const-string v2, "android.permission.WRITE_EXTERNAL_STORAGE"

    aput-object v2, v0, v1

    const/4 v1, 0x1

    const-string v2, "android.permission.READ_PHONE_STATE"

    aput-object v2, v0, v1

    return-object v0
.end method

.method public getPermissionsRequestCode()I
    .locals 1

    .prologue
    .line 69
    const/16 v0, 0x2710

    return v0
.end method

.method protected onCreate(Landroid/os/Bundle;)V
    .locals 2
    .param p1, "savedInstanceState"    # Landroid/os/Bundle;

    .prologue
    .line 47
    invoke-super {p0, p1}, Landroid/app/Activity;->onCreate(Landroid/os/Bundle;)V

    invoke-static {p0}, Lcom/playin/hook/PlayInject;->init(Landroid/content/Context;)V

    .line 50
    :try_start_0
    new-instance v1, Lcom/joypac/commonsdk/base/utils/permission/PermissionHelper;

    invoke-direct {v1, p0, p0}, Lcom/joypac/commonsdk/base/utils/permission/PermissionHelper;-><init>(Landroid/app/Activity;Lcom/joypac/commonsdk/base/utils/permission/PermissionInterface;)V

    iput-object v1, p0, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->mPermissionHelper:Lcom/joypac/commonsdk/base/utils/permission/PermissionHelper;

    .line 51
    iget-object v1, p0, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->mPermissionHelper:Lcom/joypac/commonsdk/base/utils/permission/PermissionHelper;

    invoke-virtual {v1}, Lcom/joypac/commonsdk/base/utils/permission/PermissionHelper;->requestPermissions()V
    :try_end_0
    .catch Ljava/lang/Throwable; {:try_start_0 .. :try_end_0} :catch_0

    .line 55
    :goto_0
    return-void

    .line 52
    :catch_0
    move-exception v0

    .line 53
    .local v0, "t":Ljava/lang/Throwable;
    invoke-virtual {v0}, Ljava/lang/Throwable;->printStackTrace()V

    goto :goto_0
.end method

.method public onRequestPermissionsResult(I[Ljava/lang/String;[I)V
    .locals 1
    .param p1, "requestCode"    # I
    .param p2, "permissions"    # [Ljava/lang/String;
    .param p3, "grantResults"    # [I

    .prologue
    .line 59
    iget-object v0, p0, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->mPermissionHelper:Lcom/joypac/commonsdk/base/utils/permission/PermissionHelper;

    invoke-virtual {v0, p1, p2, p3}, Lcom/joypac/commonsdk/base/utils/permission/PermissionHelper;->requestPermissionsResult(I[Ljava/lang/String;[I)Z

    move-result v0

    if-eqz v0, :cond_0

    .line 64
    :goto_0
    return-void

    .line 63
    :cond_0
    invoke-super {p0, p1, p2, p3}, Landroid/app/Activity;->onRequestPermissionsResult(I[Ljava/lang/String;[I)V

    goto :goto_0
.end method

.method public requestPermissionsFail()V
    .locals 0

    .prologue
    .line 92
    invoke-direct {p0}, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->goNextActivity()V

    .line 93
    return-void
.end method

.method public requestPermissionsSuccess()V
    .locals 0

    .prologue
    .line 86
    invoke-direct {p0}, Lcom/joypac/commonsdk/base/activity/PermissionActivity;->goNextActivity()V

    .line 87
    return-void
.end method
