package ir.atrium.core.navigation

interface AtriumNavigator {
    fun navigate(route: AtriumRoute)
    fun pop(): Boolean
    fun switchTab(tab: MainTab)
}

object NoOpAtriumNavigator : AtriumNavigator {
    override fun navigate(route: AtriumRoute) = Unit
    override fun pop(): Boolean = false
    override fun switchTab(tab: MainTab) = Unit
}
