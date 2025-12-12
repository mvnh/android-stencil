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

-keep class * implements com.app.navigation.NavigationDefinition {
    <fields>;
    <methods>;
}
-keep class com.app.navigation.** { *; }

-keep class dagger.hilt.internal.aggregatedroot.codegen.*
-keep class * implements dagger.hilt.internal.GeneratedEntryPoint
-keep class * implements dagger.hilt.internal.GeneratedComponentManager
-keep class * implements dagger.hilt.internal.GeneratedComponentManagerHolder
-keep @dagger.hilt.android.HiltAndroidApp class *
-keep @dagger.Module class * {
    @dagger.Provides *;
}
-keep @dagger.hilt.InstallIn class *

-keepattributes *Annotation*, InnerClasses, Signature, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, KotlinMetadata

-keep @kotlinx.serialization.Serializable class * { *; }
-keep class *$$serializer { *; }

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

-repackageclasses
-allowaccessmodification
-optimizations !code/allocation/variable
-dontusemixedcaseclassnames
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.slf4j.impl.StaticLoggerBinder