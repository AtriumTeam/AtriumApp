package ir.atrium.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AtriumRoute : NavKey {

    @Serializable
    data object Login : AtriumRoute

    @Serializable
    data object Register : AtriumRoute

    @Serializable
    data object Home : AtriumRoute

    @Serializable
    data object Explore : AtriumRoute

    @Serializable
    data object Activity : AtriumRoute

    @Serializable
    data object Profile : AtriumRoute

    @Serializable
    data class Compose(val kind: String) : AtriumRoute

    @Serializable
    data class Subject(val id: String) : AtriumRoute

    @Serializable
    data class Content(val id: String) : AtriumRoute

    @Serializable
    data object Settings : AtriumRoute
}

enum class MainTab(val root: AtriumRoute) {
    Home(AtriumRoute.Home),
    Explore(AtriumRoute.Explore),
    Activity(AtriumRoute.Activity),
    Profile(AtriumRoute.Profile),
}
