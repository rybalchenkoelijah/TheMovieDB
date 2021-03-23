package rybalchenko.elijah.presentation.utils.data

import androidx.paging.DataSource
import rybalchenko.elijah.data.db.MovieDao
import rybalchenko.elijah.data.entity.MovieData
import rybalchenko.elijah.data.entity.MovieDataEntityMapper
import rybalchenko.elijah.domain.entity.Movie
import javax.inject.Inject

class FavoriteMovieDataSourceFactory @Inject constructor(
    private val dataToEntityMapper: MovieDataEntityMapper,
    private val movieDao: MovieDao
) : DataSource.Factory<Int, Movie>() {

    override fun create(): DataSource<Int, Movie> =
        movieDao.getFavoriteMoviesDataSource()
            .map { input: MovieData -> dataToEntityMapper.mapFrom(input) }
}