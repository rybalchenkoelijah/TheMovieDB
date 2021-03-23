package rybalchenko.elijah.data.repository

import androidx.paging.DataSource
import io.reactivex.Completable
import rybalchenko.elijah.data.db.MovieDao
import rybalchenko.elijah.data.entity.MovieData
import rybalchenko.elijah.domain.entity.*
import javax.inject.Inject

class LocalMovieDataStore @Inject constructor(private val movieDao: MovieDao) {
    fun saveMoviesData(movies: List<MovieData>): Completable = movieDao.insertMovies(movies)

    fun updateFavoriteMovie(id: Int, isFavorite: Boolean) : Completable = movieDao.setFavoriteById(id, isFavorite)

    fun getMovieDataSourceFactory(searchParams: SearchParams): DataSource<Int, MovieData> {
        return movieDao.getMoviesDataSource(searchParams.startPeriod,
            searchParams.endPeriod)
    }

    fun getFavoriteMovieDataSourceFactory(): DataSource<Int, MovieData> {
        return movieDao.getFavoriteMoviesDataSource()
    }
}