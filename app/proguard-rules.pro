-ignorewarnings
# Save the obfuscation mapping to a file, so you can de-obfuscate any stack
# traces later on.
-printmapping build/mapping.txt

# Reduce the size of the output some more.
-repackageclasses ''
-allowaccessmodification

# Keep a fixed source file attribute and all line number tables to get line
# numbers in the stack traces.
# stack traces.

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# Preserve annotated Javascript interface methods.
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# remove Android logging calls.
-assumenosideeffects class android.util.Log {
        public static boolean isLoggable(java.lang.String, int);
        public static int v(...);
        public static int i(...);
        public static int w(...);
        public static int d(...);
}

# Kotlin
-dontwarn kotlin.**
# Remove null checkes at runtime
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

-keepclassmembers class **$WhenMappings {
    <fields>;
}

# Retrofit2
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn com.squareup.okhttp.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-keep class retrofit.** { *; }
-keepclassmembernames interface * {
    @retrofit.http.* <methods>;
}
-keepclassmembernames class * {
    @retrofit.http.* <methods>;
}
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Picasso
-dontwarn com.squareup.okhttp.**

# Gson
-keep class com.google.gson.** { *; }
-keepattributes Signature

# Keep model classes
-keep class org.kinecosystem.kinit.model.** { *; }
-keep interface org.kinecosystem.kinit.network.** { *; }
-keep class org.kinecosystem.kinit.analytics.** { *; }
-keep class android.support.v4.view.ViewPager{ *; }

# To support Enum type of class members
-keepclassmembers enum * { *; }

# Fabric & Crashlitics
-keep public class * extends java.lang.Exception
-keepattributes SourceFile
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

# Testfairy
-keep class com.testfairy.** { *; }
-dontwarn com.testfairy.**
-keepattributes Exceptions, Signature, LineNumberTable

-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}

-keep class dagger.* { *; }
-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection
-dontwarn com.google.errorprone.annotations.**