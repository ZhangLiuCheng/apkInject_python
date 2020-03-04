.class public Lio/voodoo/fabricmultidex/FabricMultiDexApplication;
.super Landroid/support/multidex/MultiDexApplication;
.source "FabricMultiDexApplication.java"


# direct methods
.method public constructor <init>()V
    .locals 0

    .line 10
    invoke-direct {p0}, Landroid/support/multidex/MultiDexApplication;-><init>()V

    return-void
.end method


# virtual methods
.method public onCreate()V
    .locals 1

    .line 13
    invoke-super {p0}, Landroid/support/multidex/MultiDexApplication;->onCreate()V

    .line 14
    sget-object v0, Lio/fabric/unity/android/FabricInitializer$Caller;->Android:Lio/fabric/unity/android/FabricInitializer$Caller;

    invoke-static {p0, v0}, Lio/fabric/unity/android/FabricInitializer;->initializeFabric(Landroid/content/Context;Lio/fabric/unity/android/FabricInitializer$Caller;)Ljava/lang/String;

    return-void
.end method
