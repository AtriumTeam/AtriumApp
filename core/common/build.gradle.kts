plugins {
    alias(libs.plugins.atrium.android.library)
    alias(libs.plugins.atrium.android.hilt)
}

android {
    namespace = "ir.atrium.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
