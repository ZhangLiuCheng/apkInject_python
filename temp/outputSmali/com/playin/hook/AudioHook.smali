.class public Lcom/playin/hook/AudioHook;
.super Ljava/lang/Object;
.source "AudioHook.java"


# direct methods
.method public constructor <init>()V
    .registers 1

    .prologue
    .line 5
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static init(Landroid/content/Context;)V
    .registers 3

    .prologue
    .line 8
    new-instance v0, Lcom/playin/hook/Test;

    const-string v1, "AudioHook Test"

    invoke-direct {v0, p0, v1}, Lcom/playin/hook/Test;-><init>(Landroid/content/Context;Ljava/lang/String;)V

    .line 9
    invoke-virtual {v0}, Lcom/playin/hook/Test;->print()V

    .line 10
    return-void
.end method
