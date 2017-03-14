# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\xujd\SDK/tools/proguard/proguard-android.txt
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
-optimizationpasses 5  #指定代码的压缩级别 0 - 7，一般都是5，无需改变
-dontusemixedcaseclassnames #不使用大小写混合
#告诉Proguard 不要跳过对非公开类的处理，默认是跳过
-dontskipnonpubliclibraryclasses #如果应用程序引入的有jar包，并且混淆jar包里面的class
-verbose #混淆时记录日志（混淆后生产映射文件 map 类名 -> 转化后类名的映射
#指定混淆时的算法，后面的参数是一个过滤器
#这个过滤器是谷歌推荐的算法，一般也不会改变
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#类型转换错误 添加如下代码以便过滤泛型（不写可能会出现类型转换错误，一般情况把这个加上就是了）,即避免泛型被混淆
-keepattributes Signature
#假如项目中有用到注解，应加入这行配置,对JSON实体映射也很重要,eg:fastjson
-keepattributes *Annotation*
#抛出异常时保留代码行数
-keepattributes SourceFile,LineNumberTable
#保持 native 的方法不去混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#保留我们使用的四大组件，自定义的Application等这些子类不被混淆
#因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}


-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}


-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}


# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


-keep public class * {
    public protected *;
}


-keep class * implements android.os.Parcelable {
   public static final android.os.Parcelable$Creator *;
 }
 #实体类
 -keep class com.xkj.trade.bean.** { *; }
 -keep class com.xkj.trade.bean_.** { *; }
#第三方开源框架以及第三方jar包中的代码不是我们的目标和关心的对象，因此我们全部忽略不进行混淆。
#EventBus的代码没必要混合
-keep class org.XXX.eventbus.** { *; }
-keep class org.XXX.eventbus.meta.** { *; }
-keep class org.XXX.event.** { *; }
-keep class org.XXX.eventbus.util.** { *; }
-keep class android.os.**{*;}
-keepclassmembers class ** {
    public void onEvent*(**);
}
#微章也不混淆
-keep class cn.bingoogolapple.badgeview.**{*;}
#gson不混淆
-keep class com.goole.gson.**{*;}
-keep class com.goole.gson.annotations.**{*;}
-keep class com.goole.gson.internal.**{*;}
-keep class com.goole.gson.internal.bind.**{*;}
-keep class com.goole.gson.internal.bind.util.**{*;}
-keep class com.goole.gson.reflect.**{*;}
-keep class com.goole.gson.stream.**{*;}
#okhttp不被混淆
#okhttp
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#circleimageview
-keep class de.hdodenhof.circleimageview.**{*;}