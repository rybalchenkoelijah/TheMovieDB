package rybalchenko.elijah.domain.usecase

import io.reactivex.Completable

import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.repository.MovieRepository
import javax.inject.Inject

interface UpdateMoviesUseCase {

    fun invoke(movie: Movie, isFavorite: Boolean): Completable
}

class UpdateFavoriteMoviesUseCase @Inject constructor(private val repository: MovieRepository) :
    UpdateMoviesUseCase {

    override fun invoke(movie: Movie, isFavorite: Boolean): Completable = repository.updateFavoriteMovie(movie, isFavorite)
}