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

-keep class com.google.bct.hook.bacotLu{*;}
-keepnames class com.google.bct.hook.bacotLu


-keep class com.google.bct.model.member{*;}
-keepnames class com.google.bct.model.member
-keepclassmembers class com.google.bct.model.** { *; }

-keep class com.google.bct.hook.bacotLu.a{*;}
-keepnames class com.google.bct.hook.bacotLu.a
#-keep class com.google.bct.hook.bacotLu.b{*;}
#-keepnames com.google.bct.hook.bacotLu.b
-keep class de.robv.android.xposed.**{*;}
-keepnames class de.robv.android.xposed.**
-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }
-dontwarn com.sendbird.android.shadow.**
-keep class com.google.bct.model.**
-dontwarn com.google.bct.model.**

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-keep class androidx.appcompat.widget.** { *; }
-dontpreverify
-repackageclasses ''
-allowaccessmodification
-flattenpackagehierarchy ''
-keepattributes EnclosingMethod
-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*,SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**
-dontwarn com.google.android.gms.internal.**
-keep class android.net.http.** { *; }
-keepclassmembers class android.net.http.** {*;}
-dontwarn android.net.**
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-dontwarn okhttp3.internal.platform.*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep class com.appodeal.** { *; }
-keep class com.amazon.** { *; }
-keep class com.mopub.** { *; }
-keep class org.nexage.** { *; }
-keep class com.applovin.** { *; }
-keep class com.chartboost.** { *; }
-keep class com.unity3d.ads.** { *; }
-keep class com.applifier.** { *; }
-keep class com.yandex.** { *; }
-keep class com.inmobi.** { *; }
-keep class com.facebook.** { *; }
-keep class ru.mail.android.mytarget.** { *; }
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.gms.common.GooglePlayServicesUtil { *; }
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
-dontwarn com.google.firebase.messaging.**
# For using GSON @Expose annotation
-keepattributes *Annotation*
-dontwarn org.xmlpull.v1.**
-keep class org.xmlpull.v1.** { *; }
-dontwarn android.arch.**
-dontwarn com.facebook.**
-dontwarn com.jirbo.adcolony.**
-dontwarn com.vungle.**
-dontwarn com.startapp.**
-dontwarn com.yandex.**
-dontwarn com.inmobi.**
-dontwarn com.chartboost.**
-dontwarn com.mopub.**
-dontwarn com.appodeal.ads.utils.**
-keep class android.support.v4.app.Fragment { *; }
-keep class android.support.v4.app.FragmentActivity { *; }
-keep class android.support.v4.app.FragmentManager { *; }
-keep class android.support.v4.app.FragmentTransaction { *; }
-keep class android.support.v4.content.LocalBroadcastManager { *; }
-keep class android.support.v4.util.LruCache { *; }
-keep class android.support.v4.view.PagerAdapter { *; }
-keep class android.support.v4.view.ViewPager { *; }
-keep class com.firebase.** { *; }



-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-assumenosideeffects class android.util.Log {
    public static *** d(...);
}

-dontwarn sun.misc.Unsafe
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn javax.annotation.CheckReturnValue
-dontwarn javax.annotation.CheckForNull
-dontwarn javax.annotation.concurrent.GuardedBy
-dontwarn javax.annotation.concurrent.Immutable
-dontwarn javax.annotation.concurrent.ThreadSafe
-dontwarn javax.annotation.concurrent.NotThreadSafe

-dontwarn kotlinx.coroutines.flow.**inlined**
-dontwarn kotlinx.coroutines.flow.**

-dontwarn kotlinx.**.*