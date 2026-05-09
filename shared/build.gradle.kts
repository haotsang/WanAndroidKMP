import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidLibrary {
        namespace = "com.haotsang.wanandroidkmp.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        androidResources {
            enable = true
        }
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.coil.network.okhttp)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.androidx.nav3.ui)
//            implementation(libs.androidx.nav3.viewmodel)
            implementation(libs.androidx.material3.adaptive)
            implementation(libs.androidx.material3.adaptive.nav3)
            implementation(libs.material3.window.size)

            implementation(libs.webview.multiplatform)

            implementation(libs.coil.compose)

            implementation(libs.sqlite.bundled)
            implementation(libs.androidx.room.runtime)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.kotlinx.datetime)


            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation("androidx.paging:paging-compose:3.4.0-beta01")
//            implementation("androidx.paging:paging-runtime:3.4.0-beta01")
            // DataStore library
            implementation("androidx.datastore:datastore:1.2.0")
            // The Preferences DataStore library
            implementation("androidx.datastore:datastore-preferences:1.2.0")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        iosMain.dependencies {
//            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.darwin)
            implementation(libs.coil.kotr)

        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(libs.ktor.client.cio)
            implementation(libs.coil.kotr)
        }
    }
}

dependencies {
    add("androidRuntimeClasspath", compose.uiTooling)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
