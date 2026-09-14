package ir.atrium.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

/** Single accessor for the version catalog from inside convention plugins. */
internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.int(alias: String): Int =
    findVersion(alias).get().requiredVersion.toInt()

internal fun VersionCatalog.str(alias: String): String =
    findVersion(alias).get().requiredVersion

/**
 * Shared Android + Kotlin configuration applied to every module, so no module
 * repeats SDK levels, Java version or desugaring settings.
 *
 * Written against the AGP 9 DSL: `CommonExtension` is no longer generic and its
 * block-form methods (`defaultConfig { }`, `compileOptions { }`) moved to the
 * concrete extensions, so properties are set directly here.
 *
 * Java 17 is set explicitly instead of through a toolchain: the only JDK on the
 * build machine is the one bundled with Android Studio, and a toolchain would
 * force Gradle to download a second JDK.
 */
internal fun Project.configureAndroidKotlin(extension: CommonExtension) {
    extension.compileSdk = libs.int("compileSdk")
    extension.buildToolsVersion = libs.str("buildTools")
    extension.defaultConfig.minSdk = libs.int("minSdk")

    extension.compileOptions.sourceCompatibility = JavaVersion.VERSION_17
    extension.compileOptions.targetCompatibility = JavaVersion.VERSION_17
    // Required because minSdk is 23 (D-101).
    extension.compileOptions.isCoreLibraryDesugaringEnabled = true

    extensions.getByType<KotlinAndroidProjectExtension>().compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("desugar-jdk-libs").get())
    }
}
