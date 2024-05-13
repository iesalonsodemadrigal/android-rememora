plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    //Hilt
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    //Room
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.iesam.rememora"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.iesam.rememora"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "0.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField("boolean", "IS_LOCAL_ENV", "false")
        }

        getByName("debug") {
            //applicationIdSuffix = ".debug"
            isDebuggable = true
            buildConfigField("boolean", "IS_LOCAL_ENV", "false")
        }

        create("local") {
            initWith(getByName("debug"))
            buildConfigField("boolean", "IS_LOCAL_ENV", "true")
            applicationIdSuffix = ".v4"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //Skeleton
    implementation("com.faltenreich:skeletonlayout:5.0.0")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //Loggin interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    //Gson
    implementation("com.google.code.gson:gson:2.10.1")
    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    //Navigation Component
    val nav_version = "2.7.6"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    //fragment
    val fragment_version = "1.6.2"
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
    //Material Design
    implementation("com.google.android.material:material:1.11.0")
    //Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database")
    //FirebaseUI
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    //Exoplayer
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")
    implementation("androidx.media3:media3-common:1.2.0")
    implementation("androidx.media3:media3-session:1.2.0")
    //Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    //TapTargetView
    implementation("com.getkeepsafe.taptargetview:taptargetview:1.13.2")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}