plugins {
    alias(libs.plugins.atrium.android.library)
    alias(libs.plugins.atrium.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ir.atrium.core.security"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.tink.android)
}
