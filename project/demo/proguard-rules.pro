# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android\sdk-preview/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#
###############DroidPlugin#############
#-libraryjars lib/layoutlib.jar

-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

-keepclasseswithmembers public class com.morgoo.hook.NativeHelper {
      *** nativeHandleHookedMethod(...);
      *** nativeHa(...);
      *** nativeHb(...);
      *** nativeHc(...);
      *** nativeHd(...);
      *** nativeHe(...);
}

-dontwarn com.morgoo.**
-keep class com.morgoo.** { *;}
-keep class android.util.Singleton{*;}
-keep class android.app.IServiceConnection{*;}
###############DroidPlugin#############

###############FileDownloader#############
# Parcelable
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# okhttp
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

-dontwarn okio.**
###############FileDownloader#############

###############RxPermissions#############
-dontwarn com.tbruyelle.rxpermissions.**
###############RxPermissions#############

###############RxJava#############
-keep class rx.** {*;}
-dontwarn rx.**
###############RxJava#############

