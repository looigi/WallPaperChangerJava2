plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.looigi.wallpaperchanger2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.looigi.wallpaperchanger2"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }

}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.acra.mail)
    implementation(libs.acra.core)
    implementation(libs.acra.toast)
    implementation(libs.acra.notification)

    implementation(libs.ksoap2.android)

    implementation(libs.android.gif.drawable)
}