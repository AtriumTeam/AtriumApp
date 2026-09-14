import com.android.build.api.dsl.LibraryExtension
import ir.atrium.buildlogic.configureAndroidKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        // AGP 9 compiles Kotlin itself; applying org.jetbrains.kotlin.android
        // here is now an error.
        pluginManager.apply("com.android.library")

        extensions.configure<LibraryExtension> {
            configureAndroidKotlin(this)

            // Library modules don't ship a BuildConfig unless they earn one.
            buildFeatures.buildConfig = false
        }
    }
}
