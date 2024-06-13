import java.io.FileInputStream
import java.util.Properties

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

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.iesam.rememora"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.iesam.rememora"
        minSdk = 26
        targetSdk = 34
        versionCode = 5
        versionName = "0.5.0"
        setProperty("archivesBaseName", "rememora-$versionName-$versionCode")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField("boolean", "IS_LOCAL_ENV", "false")
            signingConfig = signingConfigs.getByName("release")
        }

        getByName("debug") {
            //applicationIdSuffix = ".debug"
            isDebuggable = true
            buildConfigField("boolean", "IS_LOCAL_ENV", "false")
        }

        create("local") {
            initWith(getByName("debug"))
            //applicationIdSuffix = ".debugLocal" }
            buildConfigField("boolean", "IS_LOCAL_ENV", "true")
        }
        create("local-release") {
            initWith(getByName("release"))
            //applicationIdSuffix = ".debugLocal" }
            buildConfigField("boolean", "IS_LOCAL_ENV", "true")
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
        mlModelBinding = true
    }
}

androidComponents {
    onVariants(selector().all()) { variant ->
        afterEvaluate {
            val variantNameCapitalized = variant.name.replaceFirstChar { it.uppercase() }
            val buildConfigTaskName = "generate${variantNameCapitalized}BuildConfig"

            val buildConfigTask = this.project.tasks.named(buildConfigTaskName)
                .get() as com.android.build.gradle.tasks.GenerateBuildConfig

            project.tasks.getByName("ksp${variantNameCapitalized}Kotlin") {
                (this as org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool<*>).setSource(
                    buildConfigTask.sourceOutputDir
                )
            }

        }
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
    //OpenAI
    implementation(platform("com.aallam.openai:openai-client-bom:3.7.1"))
    implementation("com.aallam.openai:openai-client")
    runtimeOnly("io.ktor:ktor-client-okhttp")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //CameraX
    // CameraX core library using the camera2 implementation
    val camerax_version = "1.4.0-alpha05"
    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    // If you want to additionally use the CameraX Lifecycle library
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    // If you want to additionally use the CameraX VideoCapture library
    implementation("androidx.camera:camera-video:${camerax_version}")
    // If you want to additionally use the CameraX View class
    implementation("androidx.camera:camera-view:${camerax_version}")
    // If you want to additionally use the CameraX Extensions library
    implementation("androidx.camera:camera-extensions:${camerax_version}")

    // If you want to additionally add CameraX ML Kit Vision Integration
    implementation("androidx.camera:camera-mlkit-vision:${camerax_version}")
    implementation("com.google.mlkit:face-detection:16.1.6")

    //Permissions
    implementation("com.guolindev.permissionx:permissionx:1.7.1")

    // Tensorflow Lite dependencies
    // Import tflite dependencies
    implementation ("org.tensorflow:tensorflow-lite:2.13.0")
    // The GPU delegate library is optional. Depend on it as needed.
    //implementation ("org.tensorflow:tensorflow-lite-gpu:0.4.4")
    implementation ("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation ("org.tensorflow:tensorflow-lite-task-vision:0.4.4")
    implementation ("org.tensorflow:tensorflow-lite-metadata:0.4.4")
    //implementation ("org.tensorflow:tensorflow-lite-task-text:0.4.4")
    //implementation ("org.tensorflow:tensorflow-lite-task-audio:0.4.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}