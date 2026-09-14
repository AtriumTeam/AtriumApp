import com.android.build.api.dsl.CommonExtension
import ir.atrium.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        extensions.getByType<CommonExtension>().buildFeatures.compose = true

        dependencies {
            val bom = platform(libs.findLibrary("androidx-compose-bom").get())
            add("implementation", bom)
            add("androidTestImplementation", bom)

            listOf(
                "androidx-compose-ui",
                "androidx-compose-ui-graphics",
                "androidx-compose-ui-text",
                "androidx-compose-ui-tooling-preview",
                "androidx-compose-material3",
                "androidx-compose-material-icons-core",
            ).forEach { alias ->
                add("implementation", libs.findLibrary(alias).get())
            }

            add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        }
    }
}
