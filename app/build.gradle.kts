plugins {
    // Android-related plugins
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)

    // Dependency injection
    alias(libs.plugins.dagger.hilt)

    // Kotlin and Compose
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)

    // Crashlytics
    alias(libs.plugins.firebase.crashlytics)
}


android {
    namespace = "com.openclassrooms.hexagonal.games"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.openclassrooms.hexagonal.games"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    testOptions {
        unitTests.all {
            it.jvmArgs("-XX:+EnableDynamicAgentLoading")
        }
    }
}

dependencies {
    // -------------------------- Core Android Libraries --------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // -------------------------- Kotlin & Coroutines --------------------------
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlinx.coroutines.android)

    // -------------------------- Dependency Injection (Hilt) --------------------------
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // -------------------------- Jetpack Compose --------------------------
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.material)
    implementation(libs.compose.material3)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)

    // -------------------------- Debug tools for Compose --------------------------
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    // -------------------------- Image Loading --------------------------
    implementation(libs.coil.compose)
    implementation(libs.accompanist.permissions)

    // -------------------------- Firebase --------------------------
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.messaging.directboot)
    implementation(libs.firebase.appcheck)
    implementation(libs.firebase.appcheck.debug)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.ui.auth)
    implementation(libs.firebase.ui.storage)

    // -------------------------- Unit Testing --------------------------
    testImplementation(libs.androidx.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)

    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.junit.params)

    // -------------------------- Android Test Libraries --------------------------
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // -------------------------- Turbine for Flow testing --------------------------
    testImplementation(libs.turbine)

    // -------------------------- Mockito (core + Kotlin) --------------------------
    testImplementation(libs.mockito.kotlin)
}