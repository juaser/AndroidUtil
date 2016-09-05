# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
##############################################默认区域指令######################################

#------------------------------------------基本指令---------------------------------------------
#指定代码的压缩级别 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers
#优化  不优化输入的类文件
-dontoptimize
#预校验 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度
-dontpreverify
# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose
# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*
# 避免混淆泛型
-keepattributes Signature
-keepattributes EnclosingMethod
# 不混淆内部类
-keepattributes InnerClasses
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
#android jar
-dontwarn android.support.**
-keep class android.support.** { *; }
#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#保护所有类的公开方法
-keepclasseswithmembernames class * {
    public <methods>;
}

#忽略警告
-ignorewarning

#-----------------------------------记录生成的日志数据,gradle build时在本项目根目录输出---------------------

#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt



#-----------------------------------------Android开发中一些需要保留的公共部分------------------------------------
#保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.view.View
# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

# 保留R下面的资源
-keep class **.R$* {*;}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

# 移除Log类打印各个等级日志的代码，打正式包的时候可以做为禁log使用，这里可以作为禁止log打印的功能使用
# 记得proguard-android.txt中一定不要加-dontoptimize才起作用
# 另外的一种实现方案是通过BuildConfig.DEBUG的变量来控制
#-assumenosideeffects class android.util.Log {
#    public static int v(...);
#    public static int i(...);
#    public static int w(...);
#    public static int d(...);
#    public static int e(...);
#}



###############################################处理反射类#########################################



##############################################处理js交互##############################################



##############################################处理实体类 ##############################################
# 在开发的时候我们可以将所有的实体类放在一个包内，这样我们写一次混淆就行了。实体类是指json转换的类
#-keep public class com.mixin.daojia.bean.** { *;}

##############################################处理第三方依赖库##############################################
# ButterKnife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

#baiduMap
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

# fastJson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }

#pinyin4j
-dontwarn net.soureceforge.pinyin4j.**
-dontwarn com.hp.hpl.sparta.**
-dontwarn demo.**
-keep class net.sourceforge.pinyin4j.** { *;}
-keep class com.hp.hpl.sparta.** { *;}
-keep class demo.** { *;}

# fresco
-keep class com.facebook.fresco.** {*;}
-keep interface com.facebook.fresco.** {*;}
-keep enum com.facebook.fresco.** {*;}

-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {@com.facebook.common.internal.DoNotStrip *;}
# can not display gif image.
-keep class com.facebook.imagepipeline.animated.factory.AnimatedFactoryImpl {
    public AnimatedFactoryImpl(com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory, com.facebook.imagepipeline.core.ExecutorSupplier);
}
-keep class com.facebook.animated.gif.** {*;}
-dontwarn javax.annotation.**


# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions

# RxJava RxAndroid
-dontwarn sun.misc.**
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



#Rong_Cloud
-keepattributes Exceptions,InnerClasses

-keep class com.google.gson.examples.android.model.** { *; }

-keep class **$Properties
-dontwarn org.eclipse.jdt.annotation.**

-keep class io.agora.rtc.** {*;}

-keep class io.rong.** {*;}
-keep class io.agora.rtc.** {*; }
-keep class * implements io.rong.imlib.model.MessageContent{*;}

-dontwarn io.rong.push.** 
-dontnote com.xiaomi.** 
-dontnote com.huawei.android.pushagent.** 
-dontnote com.google.android.gms.gcm.** 
-dontnote io.rong.**
 -ignorewarnings
-keep class com.mixin.daojia.receiver.MiXinPushMessageReceiver {*;}

#dom4j
-dontwarn org.dom4j.**
#jdom
-dontwarn org.jdom2.**