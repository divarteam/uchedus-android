plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "ru.divarteam.uchedus"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.divarteam.uchedus"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storePassword = "android"
            storeFile = File("~/.android/debug.keystore")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    kotlinOptions {
        jvmTarget = "18"
    }

    kapt {
        correctErrorTypes = true
    }

    viewBinding {
        enable = true
    }
}

dependencies {
    val appCompatVersion = "1.6.1"
    val circleImageViewVersion = "3.1.0"
    val constraintLayoutVersion = "2.1.4"
    val coreKtxVersion = "1.12.0"
    val epoxyVersion = "5.1.3"
    val glideVersion = "4.16.0"
    val hiltVersion = "2.48.1"
    val jsoupVersion = "1.16.1"
    val materialVersion = "1.9.0"
    val navigationVersion = "2.7.4"
    val okHttp3Version = "5.0.0-alpha.11"
    val roomVersion = "2.5.2"
    val retrofitVersion = "2.9.0"
    val rxAndroidVersion = "2.1.1"
    val rxKotlinVersion = "2.4.0"
    val rxVersion = "2.2.21"
    val shimmerVersion = "0.5.0"
    val splashScreenVersion = "1.0.1"
    val twemoji_icons_version = "0.2.0"
    val zoomageVersion = "1.3.1"
    val junitVersion = "4.13.2"
    val junitAndroidVersion = "1.1.5"
    val espressoCoreVersion = "3.5.1"
    val gsonVersion = "2.10.1"

    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:$junitAndroidVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoCoreVersion")
    implementation("androidx.appcompat:appcompat:$appCompatVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")
    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("androidx.core:core-splashscreen:$splashScreenVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-rxjava2:$roomVersion")
    implementation("com.airbnb.android:epoxy:$epoxyVersion")
    implementation("com.facebook.shimmer:shimmer:$shimmerVersion")
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    implementation("com.google.android.material:material:$materialVersion")
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("com.jsibbold:zoomage:$zoomageVersion")
    implementation("com.squareup.okhttp3:okhttp:$okHttp3Version")
    implementation("de.hdodenhof:circleimageview:$circleImageViewVersion")
    implementation("org.jsoup:jsoup:$jsoupVersion")
    implementation("io.reactivex.rxjava2:rxandroid:$rxAndroidVersion")
    implementation("io.reactivex.rxjava2:rxjava:$rxVersion")
    implementation("io.reactivex.rxjava2:rxkotlin:$rxKotlinVersion")
    kapt("com.airbnb.android:epoxy-processor:$epoxyVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-scalars:$retrofitVersion")
}