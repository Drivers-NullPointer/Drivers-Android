import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.koverAndroidReport)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.ksp)
}

val apiKeyDev: String = project.findProperty("drivers_api_dev").toString()
val apiKeyProd: String = project.findProperty("drivers_api_prod").toString()

android {

    namespace = "com.nullpointer.devs.drivers"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nullpointer.devs.drivers"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String","drivers_api",apiKeyDev)
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String","drivers_api",apiKeyProd)
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Datastore
    implementation(libs.security.crypto.datastore.preferences)
    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
    // mockk
    testImplementation(libs.mockk)
    // Coroutines
    implementation(libs.kotlinx.coroutines.test)
    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)
    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    // destination
    implementation(libs.compose.destination)
    ksp(libs.ksp)
    // timber
    implementation(libs.timber)
    implementation(libs.logger)

}

kapt {
    correctErrorTypes = true
}

kover{
    reports{
        filters{
            excludes{
                classes(
                        "**.BuildConfig*",
                        "**.R*",
                        "**.Manifest*",
                        "**.Dagger*",
                        "**.Hilt*",
                        "**.DataStoreModule*",
                        "**HiltModule*",
                        "**_Factory*",
                    "**FileLoggingTree*",
                    "**ScreenState*",
                    "**SigningInterceptor*",
                    "**TimberLoggingInterceptor*",
                    "**TokenAuthenticator*",
                )
                packages(
                    "**.ui*",
                    "**.di*",
                    "**.model*",
                    "**.dagger*",
                    "**hilt*",
                    "**.model*",
                    "**.exceptions.*",
                    "**ramcosta**",
                )
                annotatedBy("androidx.compose.ui.tooling.preview.Preview")
                annotatedBy("androidx.compose.runtime.Composable")
            }
        }
        verify{
            rule("Line coverage") {
                bound{
                    aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                    coverageUnits = CoverageUnit.LINE
                    minValue = 100
                }
            }
            rule("Instruction coverage") {
                bound{
                    aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                    coverageUnits = CoverageUnit.INSTRUCTION
                    minValue = 100
                }
            }
        }
    }
}