plugins {
    id("com.android.application")
    id("kotlin-android")
}

val composeVersion = "1.0.1"

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "ru.awawa.clockutils"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
        kotlinCompilerVersion = "1.4.32"
    }
}

dependencies {
    val roomVersion = "2.3.0"
    val systemUiControllerVersion = "0.16.1"

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")

    implementation("com.google.accompanist:accompanist-systemuicontroller:$systemUiControllerVersion")

    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")

    implementation("androidx.navigation:navigation-compose:2.4.0-alpha06")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-beta02")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.activity:activity-compose:1.3.1")
    
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-service:2.3.1")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.7")

    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
}