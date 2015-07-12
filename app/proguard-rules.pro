# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/dehua/Desktop/android/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
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

-optimizationpasses 5
-dontusemixedcaseclassnames
-ignorewarning
-dontskipnonpubliclibraryclasses
-dontpreverify
-keepattributes SourceFile,LineNumberTable
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-libraryjars /libs/alipaysdk.jar
-libraryjars /libs/alipaysecsdk.jar
-libraryjars /libs/alipayutdid.jar
-libraryjars /libs/AndroidAnimations.jar
-libraryjars /libs/baidumapapi_v3_2_0.jar
-libraryjars /libs/easemobchat_2.2.0.jar
-libraryjars /libs/gradle-wrapper.jar
-libraryjars /libs/gson-2.2.4.jar
-libraryjars /libs/locSDK_3.3.jar
-libraryjars /libs/NiceOldAndroid-2.4.0.jar
-libraryjars /libs/ShareSDK-Core-2.3.8.jar
-libraryjars /libs/ShareSDK-QQ-2.3.8.jar
-libraryjars /libs/ShareSDK-QZone-2.3.8.jar
-libraryjars /libs/ShareSDK-ShortMessage-2.3.8.jar
-libraryjars /libs/ShareSDK-SinaWeibo-2.3.8.jar
-libraryjars /libs/ShareSDK-Wechat-2.3.8.jar
-libraryjars /libs/ShareSDK-Wechat-Core-2.3.8.jar
-libraryjars /libs/ShareSDK-Wechat-Favorite-2.3.8.jar
-libraryjars /libs/ShareSDK-Wechat-Moments-2.3.8.jar
-libraryjars /libs/umeng-analytics-v5.4.1.jar
-libraryjars /libs/umeng-update-v2.5.0.jar
-libraryjars /libs/volley.jar
-libraryjars /libs/xUtils-2.6.9.jar
-libraryjars /libs/zxing3.jar
#-libraryjars libs/armeabi/libBaiduMapSDK_v3_2_0_15.so
#-libraryjars libs/armeabi/libbspatch.so
#-libraryjars libs/armeabi/liblocSDK3d.so
#-libraryjars libs/armeabi/libvi_voslib.so
#-libraryjars libs/armeabi/libwebrtc_jni.so

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}


# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# 保持哪些类不被混淆
-keep class android.** {*; }
-keep public class * extends android.view
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.pm
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#-libraryjars ../CityListView/bin/CityListView.jar
-keep class com.quark.citylistview.** { *;}
#-libraryjars ../XListView/bin/XListView.jar
-keep class me.maxwin.** { *;}
-keep class me.maxwin.view** { *;}
-dontwarn org.slf4j.**
-dontwarn org.apache.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.codec.binary.**
-keep class com.daimajia.** { *;}
-dontwarn com.daimajia.**
# baidu
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}
#自定义控件不要混淆
-keep public class * extends android.view.View {*;}
#adapter也不能混淆
-keep public class * extends android.widget.BaseAdapter {*;}
#数据模型不要混淆
-keepclassmembers class * implements java.io.Serializable {*;}
-keepattributes *Annotation*
-dontwarn com.google.zxing.**
-keep  class com.google.zxing.**{*;}
# 使用的x_utils
-dontwarn com.lidroid.**
-keep class com.lidroid.** { *; }
-keep class * extends java.lang.annotation.Annotation { *; }
# 混淆环信
-keep class com.easemob.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}
-dontwarn  com.easemob.**
-keep class com.easemob.chatuidemo.utils.SmileUtils {*;}
-dontwarn ch.imvs.**
-dontwarn org.slf4j.**
-keep class org.ice4j.** {*;}
-keep class net.java.sip.** {*;}
-keep class org.webrtc.voiceengine.** {*;}
-keep class org.bitlet.** {*;}
-keep class org.slf4j.** {*;}
-keep class ch.imvs.** {*;}
# 环信end
# 支付宝
-keep class com.alipay.android.app.IAliPay{*;}
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.lib.ResourceMap{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.mobilesecuritysdk.*
-keep class com.ut.*
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage { *;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
#极光
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
#高德地图
-libraryjars libs/Android_Location_V1.3.2.jar
-keep   class com.amap.api.location.**{*;}
-keep   class com.aps.**{*;}
-keep   class com.amap.api.services.**{*;}
# volley jar包
-dontwarn com.android.volley.jar.**
-keep class vi.com.gdi.bgl.android.**{*;}
-keep class com.alibaba.fastjson.**{*;}
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}
-keep class com.umeng.scrshot.**
-keep class com.umeng.socialize.sensor.**
-keep class com.baidu.** { *; }
-keep class com.google.**{*;}
-keep class com.cheshifu.model.**{*;}
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }
#  sharesdk
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*
# universal-image-loader 混淆
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** {*;}
-dontwarn universal.image.loader.**
-keep class universal.image.loader.** { *;}
-keep class com.nineoldandroids.** { *; }
-keep class com.nineoldandroids.animation.** { *; }
-keep class com.nineoldandroids.util.** { *; }
-keep class com.nineoldandroids.view.** { *; }
# umeng
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep public class [com.qingmu.jianzhidaren].R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class com.umeng.fb.** { *;}
-keep public class com.umeng.fb.ui.ThreadView { }
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class com.quark.model.AllJianzhiDetail{*;}
-keep class com.quark.model.AuthenticationResponse{*;}
-keep class com.quark.model.BaomingList{*;}
-keep class com.quark.model.CancelApply{*;}
-keep class com.quark.model.Function{*;}
-keep class com.quark.model.GuangchangModle{*;}
-keep class com.quark.model.HuanxingUserInfo{*;}
-keep class com.quark.model.HuanxingUser{*;}
-keep class com.quark.model.LoginResponse{*;}
-keep class com.quark.model.Me{*;}
-keep class com.quark.model.MyAchievementModel{*;}
-keep class com.quark.model.MyJianzhi{*;}
-keep class com.quark.model.MyResume{*;}
-keep class com.quark.model.PublishAvailability{*;}
-keep class com.quark.model.PublishJianzhi{*;}
-keep class com.quark.model.ResumeToCompany{*;}
-keep class com.quark.model.Roster{*;}
-keep class com.quark.model.RosterActivityList{*;}
-keep class com.quark.model.RosterCancel{*;}
-keep class com.quark.model.RosterComfiy{*;}
-keep class com.quark.model.RosterModel{*;}
-keep class com.quark.model.RosterUser{*;}
-keep class com.quark.model.SignPersonList{*;}
-keep class com.quark.model.User{*;}
-keep class com.quark.model.UserCommentModle{*;}
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
