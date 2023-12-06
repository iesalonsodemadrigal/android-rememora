plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    //Hilt
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.iesam.rememora"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.iesam.rememora"
        minSdk = 26
        targetSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    //ViewBinding
    buildFeatures{
        viewBinding=true
    }
}

dependencies {
    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //Skeleton
    implementation ("com.faltenreich:skeletonlayout:5.0.0")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //Loggin interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    //Gson
    implementation ("com.google.code.gson:gson:2.10.1")
    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    //Navigation Component
    val nav_version = "2.7.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    //fragment
    val fragment_version = "1.6.2"
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
    //Material Design
    implementation("com.google.android.material:material:1.10.0")
    //Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")
    //Firebase
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation (platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}