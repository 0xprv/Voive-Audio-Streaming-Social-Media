# Thousands Of Talks In Your Hand
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
-keep class io.agora.**{*;}
-keepattributes SourceFile,LineNumberTable
-keepnames class com.parse.** { *; }
-keepattributes *Annotation*
-keepattributes Signature
-dontwarn android.net.SSLCertificateSocketFactory
-dontwarn android.app.Notification
-dontwarn com.squareup.**
-dontwarn okio.**


-keep public class com.google.firebase.** { public *; }
-keep class com.google.firebase.** { *; }
-dontnote com.google.firebase.**
-dontnote com.google.android.gms.internal.**
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
-dontwarn org.xmlpull.v1.**
-dontnote org.xmlpull.v1.**
-keep class org.xmlpull.** { *; }
-keepclassmembers class org.xmlpull.** { *; }

-keep class com.mapbox.android.telemetry.**
-keep class com.mapbox.android.core.location.**
-keep class android.arch.lifecycle.** { *; }
-keep class com.google.firebase.auth.**
-keep class com.mapbox.android.core.location.** { *; }
-dontnote com.mapbox.mapboxsdk.**
-dontnote com.mapbox.android.gestures.**
-dontnote com.mapbox.mapboxsdk.plugins.**

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn com.mapbox.android.core.location.LocationEngineListener
-dontwarn com.zhihu.matisse
-dontwarn okio.BufferedSink
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}


-obfuscationdictionary "F:\Android\sdk\tools\dic.txt"
-classobfuscationdictionary "F:\Android\sdk\tools\dic.txt"
-packageobfuscationdictionary "F:\Android\sdk\tools\dic.txt"

-overloadaggressively
