plugins {
    alias(libs.plugins.atrium.android.library)
    alias(libs.plugins.atrium.android.compose)
    alias(libs.plugins.atrium.android.hilt)
}

android {
    namespace = "ir.atrium.feature.explore"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:data"))
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)
}
