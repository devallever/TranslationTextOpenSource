# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#
#LitePal 数据库---------------------------------------------------------------------------------
-keep class org.litepal.** {
    *;
}

-keep class * extends org.litepal.crud.DataSupport {
    *;
}

-keep class * extends org.litepal.crud.LitePalSupport {
    *;
}
#LitePal 数据库---------------------------------------------------------------------------------





# okhttp-----------------------------------------------------------------------------------------
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# okhttp-----------------------------------------------------------------------------------------

# Retrofit-----------------------------------------------------------------------------------------
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
# Retrofit-----------------------------------------------------------------------------------------

# RxJava RxAndroid----------------------------------------------------------------------------------
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
# RxJava RxAndroid----------------------------------------------------------------------------------

# Gson-----------------------------------------------------------------------------------------
# Gson--------------------------------------------------------------------------


#
# eventbus------------------------------------------------------------------
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# eventbus------------------------------------------------------------------
#
#
##com.haibin:calendarview 日历控件------------------------------------------------------------------
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context);
#}
##com.haibin:calendarview 日历控件------------------------------------------------------------------
#
#

# 友盟------------------------------------------------------------------------------------------------
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 友盟特有-----------------------------------------------------------------------------------------------------------------
# 填应用包名
-keep public class [com.allever.app.translation.assistant].R$*{
    public static final int *;
}
#
## EventBus--------------------------------------------------------------------------------------------------------------
#-keepattributes *Annotation*
#-keepclassmembers class * {
#    @org.greenrobot.eventbus.Subscribe <methods>;
#}
#-keep enum org.greenrobot.eventbus.ThreadMode { *; }
#
## Only required if you use AsyncExecutor
#-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
#    <init>(java.lang.Throwable);
#}
## EventBus----------------------------------------------------------------------------------
#
## Glide----------------------------------------------------------------------------------------------------------------
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public class * extends com.bumptech.glide.module.AppGlideModule
#-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}
#
### for DexGuard only
##-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
### Glide----------------------------------------------------------------------------------
#


# 奇葩问题
# Process: com.allever.app.virtual.call, PID: 28185
#    java.lang.NoSuchMethodError: No static method asAttributeSet(Lg/a/a/a;)Landroid/util/AttributeSet; in class Landroid/util/Xml; or its super classes (declaration of 'android.util.Xml' appears in /system/framework/framework.jar)
# https://www.jianshu.com/p/48c56e9048e7
-dontwarn org.xmlpull.v1.XmlPullParser
-dontwarn org.xmlpull.v1.XmlSerializer
-keep class org.xmlpull.v1.* {*;}

# 百度语音识别
-keep class com.baidu.speech.**{ *;}

#讯飞ad
#添加防止SDK被混淆
-dontwarn com.iflytek.**
-keep class com.iflytek.**{*;}
-keep class android.support.v4.**{public * ;}


# Facebook广告
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ADMOB-----------------------------------------------------------------------
# For Google Play Services
-keep public class com.google.android.gms.ads.**{
   public *;
}

# For old ads classes
-keep public class com.google.ads.**{
   public *;
}

# For mediation
-keepattributes *Annotation*

# Other required classes for Google Play Services
# Read more at http://developer.android.com/google/play-services/setup.html
-keep class * extends java.util.ListResourceBundle {
   protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
   public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
   @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
   public static final ** CREATOR;
}

# ADMOB-----------------------------------------------------------------------

# 米盟
-keep class com.xiaomi.ad.**{*;}
-keep class com.miui.zeus.**{*;}

# 推荐模块------------------------------------------------------------------------
# okhttp-----------------------------------------------------------------------------------------
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# okhttp-----------------------------------------------------------------------------------------

# Retrofit-----------------------------------------------------------------------------------------
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
# Retrofit-----------------------------------------------------------------------------------------

# RxJava RxAndroid----------------------------------------------------------------------------------
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
# RxJava RxAndroid----------------------------------------------------------------------------------

# Gson-----------------------------------------------------------------------------------------
# Gson--------------------------------------------------------------------------

# 推荐模块------------------------------------------------------------------------