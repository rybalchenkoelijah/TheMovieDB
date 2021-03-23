package rybalchenko.elijah.domain.usecase

import androidx.paging.DataSource
import io.reactivex.Completable
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.repository.MovieRepository
import javax.inject.Inject


interface FavoriteMovieDataSourceUseCase {
    fun createFavoriteMovieDataSourceFactory(): DataSource<Int, Movie>
    fun updateFavoriteMovie(movie: Movie, isFavorite: Boolean): Completable
}

class FavoriteMovieDataSourceUseCaseImpl @Inject constructor(val repository: MovieRepository) :
    FavoriteMovieDataSourceUseCase {

    override fun createFavoriteMovieDataSourceFactory(): DataSource<Int, Movie> {
        return repository.getFavoriteMovieDataSourceFactory()
    }

    override fun updateFavoriteMovie(
        movie: Movie,
        isFavorite: Boolean
    ): Completable {
        return repository.updateFavoriteMovie(movie, isFavorite)
    }
}