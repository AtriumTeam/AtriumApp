// Repository order matters here.
//
// Google's Maven repository (dl.google.com / maven.google.com) answers 404 for
// every artifact on the target market's network, so AGP and all AndroidX
// artifacts are unreachable through it. Mirrors are listed first and google()
// is kept last as a fallback for anyone building through a VPN. This is a
// direct consequence of D-002 and applies to every build of this project.
//
// Keep this list in sync with build-logic/settings.gradle.kts.

pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven("https://maven.myket.ir")
        maven("https://maven.aliyun.com/repository/google")
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://maven.myket.ir")
        maven("https://maven.aliyun.com/repository/google")
        mavenCentral()
        google()
    }
}

rootProject.name = "atrium"

include(":app")
include(":core:common")
include(":core:model")
include(":core:designsystem")
include(":core:navigation")
include(":core:security")
include(":core:database")
include(":core:data")
include(":feature:auth")
include(":feature:home")
include(":feature:explore")
include(":feature:create")
include(":feature:activity")
include(":feature:account")
include(":feature:catalog")
