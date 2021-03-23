package rybalchenko.elijah.presentation.navigation

import androidx.navigation.NavController
import dagger.Lazy
import rybalchenko.elijah.presentation.utils.navigation.executeSafeNavAction
import rybalchenko.elijah.test.R
import javax.inject.Inject

interface HomeNavigator {

    fun navigateToMain()

}

class HomeNavigatorImpl @Inject constructor(
    private val navController: Lazy<NavController>
) : HomeNavigator {

    override fun navigateToMain() {
        executeSafeNavAction {
            navController.get().navigate(R.id.action_show_movies)
        }
    }
}