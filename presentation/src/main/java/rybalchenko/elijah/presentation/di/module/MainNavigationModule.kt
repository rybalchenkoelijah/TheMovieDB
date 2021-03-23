package rybalchenko.elijah.presentation.di.module

import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.Module
import dagger.Provides
import rybalchenko.elijah.presentation.di.scopes.ActivityScope
import rybalchenko.elijah.presentation.navigation.HomeCoordinator
import rybalchenko.elijah.presentation.navigation.HomeCoordinatorImpl
import rybalchenko.elijah.presentation.navigation.HomeNavigator
import rybalchenko.elijah.presentation.navigation.HomeNavigatorImpl
import rybalchenko.elijah.presentation.sections.home.HomeActivity
import rybalchenko.elijah.test.R

@Module
class MainNavigationModule {

    @Provides
    @ActivityScope
    fun provideNavController(activity: HomeActivity): NavController =
        Navigation.findNavController(activity, R.id.mainNavigationFragment)

    @Provides
    fun bindNavigator(navigatorImpl: HomeNavigatorImpl): HomeNavigator {
        return navigatorImpl
    }

    @Provides
    fun bindCoordinator(coordinatorImpl: HomeCoordinatorImpl): HomeCoordinator {
        return coordinatorImpl
    }
}