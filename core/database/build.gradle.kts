plugins {
    alias(libs.plugins.atrium.android.library)
    alias(libs.plugins.atrium.android.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ir.atrium.core.database"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}
