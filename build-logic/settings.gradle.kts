// See the comment in the root settings.gradle.kts: Google's Maven repository is
// unreachable from the target market's network, so mirrors come first.

dependencyResolutionManagement {
    repositories {
        maven("https://maven.myket.ir")
        maven("https://maven.aliyun.com/repository/google")
        mavenCentral()
        gradlePluginPortal()
        google()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    repositories {
        maven("https://maven.myket.ir")
        maven("https://maven.aliyun.com/repository/google")
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "build-logic"

include(":convention")
