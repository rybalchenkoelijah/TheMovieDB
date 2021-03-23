package rybalchenko.elijah.presentation.sections.splash

import rybalchenko.elijah.presentation.base.BaseViewModel
import rybalchenko.elijah.presentation.navigation.HomeCoordinator
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val navCoordinator: HomeCoordinator) : BaseViewModel() {

    fun onAppLaunch() {
        navCoordinator.start()
    }
}