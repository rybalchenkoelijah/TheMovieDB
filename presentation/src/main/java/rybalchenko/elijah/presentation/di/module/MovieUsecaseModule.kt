package rybalchenko.elijah.presentation.di.module

import dagger.Binds
import dagger.Module
import rybalchenko.elijah.domain.usecase.*
import javax.inject.Named
import javax.inject.Singleton

@Module
interface MoviesUseCaseModule {

    @Binds
    @Singleton
    fun createMoviesUseCase(useCase: FindAllMoviesUseCase): FindMoviesUseCase

    @Binds
    @Singleton
    fun createUpdateMoviesUseCase(useCase: UpdateFavoriteMoviesUseCase): UpdateMoviesUseCase
}