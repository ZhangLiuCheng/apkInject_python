.class public Lcom/playin/apkinject/InjectApplication;
.super Landroid/app/Application;
.source "InjectApplication.java"


# direct methods
.method public constructor <init>()V
    .locals 0

    .line 8
    invoke-direct {p0}, Landroid/app/Application;-><init>()V

    return-void
.end method


# virtual methods
.method public onCreate()V
    .locals 2

    .line 12
    invoke-super {p0}, Landroid/app/Application;->onCreate()V

    invoke-static {p0}, Lcom/playin/hook/AudioHook;->init(Landroid/content/Context;)V

    .line 13
    const-string v0, "APK_INJECT"

    const-string v1, "InjectApplication -----> onCreate"

    invoke-static {v0, v1}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 15
    invoke-static {p0}, Lcom/playin/hook/AudioHook;->init(Landroid/content/Context;)V

    .line 16
    return-void
.end method
