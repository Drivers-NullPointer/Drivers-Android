import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.koverAndroidReport)
}



tasks.register("runTestsAndCheckCoverage") {
    dependsOn("testDebugUnitTest", "jacocoTestReport", "jacocoTestCoverageVerification")
}


tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    group = "Reporting"
    description = "Generate Jacoco coverage reports after running tests."

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(true)
    }

    val exclusions = listOf(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "**/ui/**",
        "**/di/**",
        "**/model/**"
    )


    // Excluir clases Java y Kotlin
    val javaClasses = fileTree(layout.buildDirectory.dir("intermediates/javac/debug")) {
        exclude(exclusions)
    }

    val kotlinClasses = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        exclude(exclusions)
    }

    // Si también deseas agregar la ruta `intermediates/javac/` para las clases Java:
    val additionalJavaClasses = fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
        exclude(exclusions)
    }

    val sources = files(
        "$projectDir/src/main/java",
        "$projectDir/src/main/kotlin"
    )

    sourceDirectories.setFrom(sources)
    classDirectories.setFrom(javaClasses, kotlinClasses, additionalJavaClasses)
    executionData.setFrom(layout.buildDirectory.file("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"))
}

tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    dependsOn("jacocoTestReport")

    violationRules {
        rule {
            enabled = true
            limit {
                counter = "LINE"
                value = "TOTALCOUNT"
                minimum = 0.8.toBigDecimal()
            }
        }
    }
}


android {


    testCoverage {
        version = "0.8.12"
    }

    namespace = "com.nullpointer.devs.drivers"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nullpointer.devs.drivers"
        minSdk = 21
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
        }
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
    buildFeatures {
        compose = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Datastore
    debugImplementation(libs.androidx.datastore.preferences)
    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
    // mockk
    testImplementation(libs.mockk)
    // Coroutines
    implementation(libs.kotlinx.coroutines.test)

}


kover{
    reports{
        filters{
            excludes{
                packages("**.ui**")
                packages("**.model.*")
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
            rule("Branch coverage") {
                bound{
                    aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                    coverageUnits = CoverageUnit.BRANCH
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