plugins {
    alias(libs.plugins.atrium.android.library)
    alias(libs.plugins.atrium.android.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ir.atrium.core.designsystem"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.androidx.core.ktx)
}
