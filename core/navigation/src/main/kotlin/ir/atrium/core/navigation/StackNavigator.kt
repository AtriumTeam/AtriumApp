package ir.atrium.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

class StackNavigator(
    private val backStack: NavBackStack<NavKey>,
) : AtriumNavigator {
    override fun navigate(route: AtriumRoute) {
        backStack.add(route)
    }

    override fun pop(): Boolean {
        if (backStack.size <= 1) return false
        backStack.removeLastOrNull()
        return true
    }

    override fun switchTab(tab: MainTab) = Unit
}

@Composable
fun rememberStackNavigator(start: AtriumRoute): Pair<NavBackStack<NavKey>, AtriumNavigator> {
    val backStack = rememberNavBackStack(start)
    val navigator = remember(backStack) { StackNavigator(backStack) }
    return backStack to navigator
}
