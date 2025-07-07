plugins {
    alias(libs.plugins.android.application)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

secrets {
    // To add your Maps API key to this project:
    // 1. If the secrets.properties file does not exist, create it in the same folder as the local.properties file.
    // 2. Add this line, where YOUR_API_KEY is your API key:
    //        MAPS_API_KEY=YOUR_API_KEY
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"

    // Configure which keys should be ignored by the plugin by providing regular expressions.
    // "sdk.dir" is ignored by default.
    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
    ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
}

android {
    namespace = "com.looigi.wallpaperchanger2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.looigi.wallpaperchanger2"
        minSdk = 31
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

    applicationVariants.all {
        val variant = this
        variant.outputs.forEach { output ->
            // val apkName = "MyApp-${variant.versionName}-${variant.name}.apk"
            val apkName = "Wallpaper Changer II.apk"
            (output as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName = apkName
        }
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

    implementation(libs.play.services.location)

    implementation(libs.gson)

    implementation (libs.android.mail.v162)
    implementation (libs.android.activation)

    implementation(libs.ksoap2.android)

    implementation(libs.android.gif.drawable)
    implementation(libs.photoview)
    implementation(libs.play.services.maps)

    implementation(libs.face.detection)
    implementation(libs.android.image.cropper)

    implementation(libs.core)
    implementation(libs.media)

    implementation(libs.language.id)

    implementation(libs.play.services.auth.v2070)
    implementation(libs.google.api.client.android.v1341)
    implementation(libs.google.api.services.drive.vv3rev2021250)
    implementation(libs.play.services.auth.v2130)
    implementation(libs.google.api.client.gson)
    implementation(libs.google.http.client.android)
    implementation(libs.google.api.services.drive)

    implementation(libs.work.runtime)
}