package rybalchenko.elijah.presentation.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import rybalchenko.elijah.presentation.di.scopes.FragmentScope
import rybalchenko.elijah.presentation.sections.movies.FavoriteMoviesFragment
import rybalchenko.elijah.presentation.sections.movies.MainFragment
import rybalchenko.elijah.presentation.sections.movies.MoviesFragment
import rybalchenko.elijah.presentation.sections.splash.SplashFragment


@Module
interface FragmentBuildersModule {
    @FragmentScope
    @ContributesAndroidInjector
    fun createSplashFragmentInjector(): SplashFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun createMainFragmentInjector(): MainFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun createMoviesFragmentInjector(): MoviesFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun createFavoriteMoviesFragmentInjector(): FavoriteMoviesFragment




}