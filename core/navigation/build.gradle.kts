plugins {
    alias(libs.plugins.atrium.android.library)
    alias(libs.plugins.atrium.android.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ir.atrium.core.navigation"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization.core)
}
