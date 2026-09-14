plugins {
    alias(libs.plugins.atrium.android.application)
    alias(libs.plugins.atrium.android.compose)
    alias(libs.plugins.atrium.android.hilt)
}

android {
    namespace = "ir.atrium.app"

    defaultConfig {
        applicationId = "ir.atrium.app"
        versionCode = 1
        versionName = "0.1.0"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:security"))
    implementation(project(":core:database"))
    implementation(project(":core:data"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:explore"))
    implementation(project(":feature:create"))
    implementation(project(":feature:activity"))
    implementation(project(":feature:account"))
    implementation(project(":feature:catalog"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
}
