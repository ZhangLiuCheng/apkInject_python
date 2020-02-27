.class public Lcom/habby/archero/SignCheck;
.super Ljava/lang/Object;
.source "SignCheck.java"


# static fields
.field private static final TAG:Ljava/lang/String; = "SignCheck"


# instance fields
.field private cer:Ljava/lang/String;

.field private context:Landroid/content/Context;

.field private realCer:Ljava/lang/String;


# direct methods
.method public constructor <init>(Landroid/content/Context;)V
    .locals 1

    .line 23
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    .line 19
    iput-object v0, p0, Lcom/habby/archero/SignCheck;->cer:Ljava/lang/String;

    .line 20
    iput-object v0, p0, Lcom/habby/archero/SignCheck;->realCer:Ljava/lang/String;

    .line 24
    iput-object p1, p0, Lcom/habby/archero/SignCheck;->context:Landroid/content/Context;

    .line 25
    invoke-virtual {p0}, Lcom/habby/archero/SignCheck;->getCertificateSHA1Fingerprint()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lcom/habby/archero/SignCheck;->cer:Ljava/lang/String;

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Ljava/lang/String;)V
    .locals 1

    .line 28
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    .line 19
    iput-object v0, p0, Lcom/habby/archero/SignCheck;->cer:Ljava/lang/String;

    .line 20
    iput-object v0, p0, Lcom/habby/archero/SignCheck;->realCer:Ljava/lang/String;

    .line 29
    iput-object p1, p0, Lcom/habby/archero/SignCheck;->context:Landroid/content/Context;

    .line 30
    iput-object p2, p0, Lcom/habby/archero/SignCheck;->realCer:Ljava/lang/String;

    .line 31
    invoke-virtual {p0}, Lcom/habby/archero/SignCheck;->getCertificateSHA1Fingerprint()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lcom/habby/archero/SignCheck;->cer:Ljava/lang/String;

    return-void
.end method

.method private byte2HexFormatted([B)Ljava/lang/String;
    .locals 8

    .line 120
    new-instance v0, Ljava/lang/StringBuilder;

    array-length v1, p1

    const/4 v2, 0x2

    mul-int/lit8 v1, v1, 0x2

    invoke-direct {v0, v1}, Ljava/lang/StringBuilder;-><init>(I)V

    const/4 v1, 0x0

    .line 122
    :goto_0
    array-length v3, p1

    if-ge v1, v3, :cond_3

    .line 123
    aget-byte v3, p1, v1

    invoke-static {v3}, Ljava/lang/Integer;->toHexString(I)Ljava/lang/String;

    move-result-object v3

    .line 124
    invoke-virtual {v3}, Ljava/lang/String;->length()I

    move-result v4

    const/4 v5, 0x1

    if-ne v4, v5, :cond_0

    .line 126
    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "0"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    :cond_0
    if-le v4, v2, :cond_1

    add-int/lit8 v6, v4, -0x2

    .line 128
    invoke-virtual {v3, v6, v4}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v3

    .line 129
    :cond_1
    invoke-virtual {v3}, Ljava/lang/String;->toUpperCase()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 130
    array-length v3, p1

    sub-int/2addr v3, v5

    if-ge v1, v3, :cond_2

    const/16 v3, 0x3a

    .line 131
    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    :cond_2
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    .line 133
    :cond_3
    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    return-object p1
.end method


# virtual methods
.method public check()Z
    .locals 1

    const/4 v0, 0x1

    return v0
.end method

.method public getCertificateSHA1Fingerprint()Ljava/lang/String;
    .locals 4

    .line 54
    iget-object v0, p0, Lcom/habby/archero/SignCheck;->context:Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v0

    .line 58
    iget-object v1, p0, Lcom/habby/archero/SignCheck;->context:Landroid/content/Context;

    invoke-virtual {v1}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v1

    const/4 v2, 0x0

    const/16 v3, 0x40

    .line 67
    :try_start_0
    invoke-virtual {v0, v1, v3}, Landroid/content/pm/PackageManager;->getPackageInfo(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;

    move-result-object v0
    :try_end_0
    .catch Landroid/content/pm/PackageManager$NameNotFoundException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception v0

    .line 69
    invoke-virtual {v0}, Landroid/content/pm/PackageManager$NameNotFoundException;->printStackTrace()V

    move-object v0, v2

    .line 73
    :goto_0
    iget-object v0, v0, Landroid/content/pm/PackageInfo;->signatures:[Landroid/content/pm/Signature;

    const/4 v1, 0x0

    .line 74
    aget-object v0, v0, v1

    invoke-virtual {v0}, Landroid/content/pm/Signature;->toByteArray()[B

    move-result-object v0

    .line 77
    new-instance v1, Ljava/io/ByteArrayInputStream;

    invoke-direct {v1, v0}, Ljava/io/ByteArrayInputStream;-><init>([B)V

    :try_start_1
    const-string v0, "X509"

    .line 83
    invoke-static {v0}, Ljava/security/cert/CertificateFactory;->getInstance(Ljava/lang/String;)Ljava/security/cert/CertificateFactory;

    move-result-object v0
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_1

    goto :goto_1

    :catch_1
    move-exception v0

    .line 85
    invoke-virtual {v0}, Ljava/lang/Exception;->printStackTrace()V

    move-object v0, v2

    .line 92
    :goto_1
    :try_start_2
    invoke-virtual {v0, v1}, Ljava/security/cert/CertificateFactory;->generateCertificate(Ljava/io/InputStream;)Ljava/security/cert/Certificate;

    move-result-object v0

    check-cast v0, Ljava/security/cert/X509Certificate;
    :try_end_2
    .catch Ljava/lang/Exception; {:try_start_2 .. :try_end_2} :catch_2

    goto :goto_2

    :catch_2
    move-exception v0

    .line 94
    invoke-virtual {v0}, Ljava/lang/Exception;->printStackTrace()V

    move-object v0, v2

    :goto_2
    :try_start_3
    const-string v1, "SHA1"

    .line 101
    invoke-static {v1}, Ljava/security/MessageDigest;->getInstance(Ljava/lang/String;)Ljava/security/MessageDigest;

    move-result-object v1

    .line 104
    invoke-virtual {v0}, Ljava/security/cert/X509Certificate;->getEncoded()[B

    move-result-object v0

    invoke-virtual {v1, v0}, Ljava/security/MessageDigest;->digest([B)[B

    move-result-object v0

    .line 107
    invoke-direct {p0, v0}, Lcom/habby/archero/SignCheck;->byte2HexFormatted([B)Ljava/lang/String;

    move-result-object v2
    :try_end_3
    .catch Ljava/security/NoSuchAlgorithmException; {:try_start_3 .. :try_end_3} :catch_4
    .catch Ljava/security/cert/CertificateEncodingException; {:try_start_3 .. :try_end_3} :catch_3

    goto :goto_3

    :catch_3
    move-exception v0

    .line 112
    invoke-virtual {v0}, Ljava/security/cert/CertificateEncodingException;->printStackTrace()V

    goto :goto_3

    :catch_4
    move-exception v0

    .line 110
    invoke-virtual {v0}, Ljava/security/NoSuchAlgorithmException;->printStackTrace()V

    :goto_3
    return-object v2
.end method

.method public getRealCer()Ljava/lang/String;
    .locals 1

    .line 35
    iget-object v0, p0, Lcom/habby/archero/SignCheck;->realCer:Ljava/lang/String;

    return-object v0
.end method

.method public setRealCer(Ljava/lang/String;)V
    .locals 0

    .line 44
    iput-object p1, p0, Lcom/habby/archero/SignCheck;->realCer:Ljava/lang/String;

    return-void
.end method
