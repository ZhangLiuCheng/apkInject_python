.class public Lcom/playin/hook/Test;
.super Ljava/lang/Object;
.source "Test.java"


# instance fields
.field private context:Landroid/content/Context;

.field private n:Lcom/playin/hook/AudioHook;

.field private name:Ljava/lang/String;


# direct methods
.method public constructor <init>(Landroid/content/Context;Ljava/lang/String;)V
    .registers 3

    .prologue
    .line 14
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 15
    iput-object p1, p0, Lcom/playin/hook/Test;->context:Landroid/content/Context;

    .line 16
    iput-object p2, p0, Lcom/playin/hook/Test;->name:Ljava/lang/String;

    .line 17
    return-void
.end method

.method static synthetic access$000(Lcom/playin/hook/Test;)Landroid/content/Context;
    .registers 2

    .prologue
    .line 8
    iget-object v0, p0, Lcom/playin/hook/Test;->context:Landroid/content/Context;

    return-object v0
.end method

.method static synthetic access$100(Lcom/playin/hook/Test;)Ljava/lang/String;
    .registers 2

    .prologue
    .line 8
    iget-object v0, p0, Lcom/playin/hook/Test;->name:Ljava/lang/String;

    return-object v0
.end method


# virtual methods
.method public print()V
    .registers 3

    .prologue
    .line 20
    new-instance v0, Ljava/lang/Thread;

    new-instance v1, Lcom/playin/hook/Test$1;

    invoke-direct {v1, p0}, Lcom/playin/hook/Test$1;-><init>(Lcom/playin/hook/Test;)V

    invoke-direct {v0, v1}, Ljava/lang/Thread;-><init>(Ljava/lang/Runnable;)V

    .line 33
    invoke-virtual {v0}, Ljava/lang/Thread;->start()V

    .line 34
    return-void
.end method
