package rybalchenko.elijah.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import rybalchenko.elijah.data.db.MovieDao
import rybalchenko.elijah.data.entity.MovieData
import rybalchenko.elijah.data.entity.MovieListPageMapper
import rybalchenko.elijah.data.entity.MoviesPageData
import rybalchenko.elijah.domain.entity.*
import javax.inject.Inject

class LocalMovieDataStore @Inject constructor(
    private val movieDao: MovieDao,
    private val mapper: MovieListPageMapper
) : MovieDataStore {
    override fun findMoviesBySearchParams(searchParams: SearchParams): Observable<MoviesPageData> {
        return movieDao.searchMoviesInPeriod(searchParams.startPeriod,
            searchParams.endPeriod, searchParams.offset, searchParams.pageSize).map(mapper::mapFromList)
    }

    fun saveMoviesData(movies: List<MovieData>): Completable = movieDao.insertMovies(movies)

    fun updateFavoriteMovie(id: Int, isFavorite: Boolean) : Completable = movieDao.setFavoriteById(id, isFavorite)
}