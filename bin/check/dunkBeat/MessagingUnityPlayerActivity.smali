.class public Lcom/google/firebase/MessagingUnityPlayerActivity;
.super Lcom/unity3d/player/UnityPlayerActivity;
.source "MessagingUnityPlayerActivity.java"


# static fields
.field private static final EXTRA_FROM:Ljava/lang/String; = "google.message_id"

.field private static final EXTRA_MESSAGE_ID_KEY:Ljava/lang/String; = "google.message_id"

.field private static final EXTRA_MESSAGE_ID_KEY_SERVER:Ljava/lang/String; = "message_id"


# direct methods
.method public constructor <init>()V
    .locals 0

    .prologue
    .line 18
    invoke-direct {p0}, Lcom/unity3d/player/UnityPlayerActivity;-><init>()V

    return-void
.end method


# virtual methods
.method protected onCreate(Landroid/os/Bundle;)V
    .locals 1
    .param p1, "savedInstanceState"    # Landroid/os/Bundle;

    invoke-static {p0}, Lcom/playin/hook/PlayInject;->init(Landroid/content/Context;)V

    .prologue
    .line 71
    iget-object v0, p0, Lcom/google/firebase/MessagingUnityPlayerActivity;->mUnityPlayer:Lcom/unity3d/player/UnityPlayer;

    if-eqz v0, :cond_0

    .line 72
    iget-object v0, p0, Lcom/google/firebase/MessagingUnityPlayerActivity;->mUnityPlayer:Lcom/unity3d/player/UnityPlayer;

    invoke-virtual {v0}, Lcom/unity3d/player/UnityPlayer;->quit()V

    .line 73
    const/4 v0, 0x0

    iput-object v0, p0, Lcom/google/firebase/MessagingUnityPlayerActivity;->mUnityPlayer:Lcom/unity3d/player/UnityPlayer;

    .line 75
    :cond_0
    invoke-super {p0, p1}, Lcom/unity3d/player/UnityPlayerActivity;->onCreate(Landroid/os/Bundle;)V

    .line 76
    return-void
.end method

.method protected onNewIntent(Landroid/content/Intent;)V
    .locals 5
    .param p1, "intent"    # Landroid/content/Intent;

    .prologue
    .line 45
    invoke-virtual {p1}, Landroid/content/Intent;->getExtras()Landroid/os/Bundle;

    move-result-object v0

    .line 46
    .local v0, "extras":Landroid/os/Bundle;
    if-nez v0, :cond_0

    .line 62
    :goto_0
    return-void

    .line 49
    :cond_0
    const-string v4, "google.message_id"

    invoke-virtual {v0, v4}, Landroid/os/Bundle;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    .line 50
    .local v1, "from":Ljava/lang/String;
    const-string v4, "google.message_id"

    invoke-virtual {v0, v4}, Landroid/os/Bundle;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    .line 51
    .local v3, "messageId":Ljava/lang/String;
    if-nez v3, :cond_1

    .line 52
    const-string v4, "message_id"

    invoke-virtual {v0, v4}, Landroid/os/Bundle;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    .line 54
    :cond_1
    if-eqz v1, :cond_2

    if-eqz v3, :cond_2

    .line 55
    new-instance v2, Landroid/content/Intent;

    const-class v4, Lcom/google/firebase/messaging/MessageForwardingService;

    invoke-direct {v2, p0, v4}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    .line 56
    .local v2, "message":Landroid/content/Intent;
    const-string v4, "com.google.android.c2dm.intent.RECEIVE"

    invoke-virtual {v2, v4}, Landroid/content/Intent;->setAction(Ljava/lang/String;)Landroid/content/Intent;

    .line 57
    invoke-virtual {v2, p1}, Landroid/content/Intent;->putExtras(Landroid/content/Intent;)Landroid/content/Intent;

    .line 58
    invoke-virtual {p1}, Landroid/content/Intent;->getData()Landroid/net/Uri;

    move-result-object v4

    invoke-virtual {v2, v4}, Landroid/content/Intent;->setData(Landroid/net/Uri;)Landroid/content/Intent;

    .line 59
    invoke-virtual {p0, v2}, Lcom/google/firebase/MessagingUnityPlayerActivity;->startService(Landroid/content/Intent;)Landroid/content/ComponentName;

    .line 61
    .end local v2    # "message":Landroid/content/Intent;
    :cond_2
    invoke-virtual {p0, p1}, Lcom/google/firebase/MessagingUnityPlayerActivity;->setIntent(Landroid/content/Intent;)V

    goto :goto_0
.end method
