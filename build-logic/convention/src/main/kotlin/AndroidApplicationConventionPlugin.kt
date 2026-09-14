import com.android.build.api.dsl.ApplicationExtension
import ir.atrium.buildlogic.configureAndroidKotlin
import ir.atrium.buildlogic.int
import ir.atrium.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        // AGP 9 compiles Kotlin itself; applying org.jetbrains.kotlin.android
        // here is now an error.
        pluginManager.apply("com.android.application")

        extensions.configure<ApplicationExtension> {
            configureAndroidKotlin(this)

            defaultConfig.targetSdk = libs.int("targetSdk")

            buildTypes.getByName("release").apply {
                isMinifyEnabled = true
                isShrinkResources = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                )
            }

            buildTypes.getByName("debug").apply {
                applicationIdSuffix = ".debug"
                isMinifyEnabled = false
            }
        }
    }
}
