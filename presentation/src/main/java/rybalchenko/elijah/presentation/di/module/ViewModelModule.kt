package rybalchenko.elijah.presentation.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import rybalchenko.elijah.presentation.sections.movies.FavoriteMoviesViewModel
import rybalchenko.elijah.presentation.sections.movies.MainViewModel
import rybalchenko.elijah.presentation.sections.movies.MoviesViewModel
import rybalchenko.elijah.presentation.sections.splash.SplashViewModel
import rybalchenko.elijah.presentation.utils.di.ViewModelKey
import rybalchenko.elijah.presentation.utils.viewmodel.ViewModelFactory

@Module
interface ViewModelModule {

    @Binds
    fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun provideSplashViewModel(viewModel: SplashViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideMainViewModel(viewModel: MainViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    fun provideMoviesViewModel(viewModel: MoviesViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteMoviesViewModel::class)
    fun provideFavoriteMoviesViewModel(viewModel: FavoriteMoviesViewModel) : ViewModel


}