plugins {
    alias(libs.plugins.atrium.android.library)
    alias(libs.plugins.atrium.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ir.atrium.core.data"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:security"))
    implementation(project(":core:database"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
}
