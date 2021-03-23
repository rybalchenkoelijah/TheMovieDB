package rybalchenko.elijah.presentation.di.module

import dagger.Binds
import dagger.Module
import rybalchenko.elijah.domain.usecase.*
import javax.inject.Singleton

@Module
interface MoviesUseCaseModule {

    @Binds
    @Singleton
    fun createMovieDataSourceUseCase(useCase: MovieDataSourceUseCaseImpl): MovieDataSourceUseCase

    @Binds
    @Singleton
    fun createFavoriteMovieDataSourceUseCase(useCase: FavoriteMovieDataSourceUseCaseImpl): FavoriteMovieDataSourceUseCase
}