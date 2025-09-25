# Keep rules for Media3/ExoPlayer and OkHttp (generally safe defaults)
-keep class com.google.android.exoplayer2.** { *; }
-keep class androidx.media3.** { *; }
-dontwarn com.google.android.exoplayer2.**
-dontwarn androidx.media3.**
-dontwarn org.jetbrains.annotations.**

