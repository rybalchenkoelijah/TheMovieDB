package rybalchenko.elijah.data.repository

import androidx.paging.DataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import rybalchenko.elijah.data.entity.MovieDataEntityMapper
import rybalchenko.elijah.data.entity.MoviePageDataEntityMapper
import rybalchenko.elijah.data.entity.MoviesPageData
import rybalchenko.elijah.domain.entity.*
import rybalchenko.elijah.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val localMovieDataStore: LocalMovieDataStore,
    private val remoteMovieDataStore: RemoteMovieDataStore,
    private val pageEntityMapper: MoviePageDataEntityMapper,
    private val dataEntityMapper: MovieDataEntityMapper
) : MovieRepository {

    override fun updateFavoriteMovie(movie: Movie, isFavorite: Boolean): Completable =
        localMovieDataStore.updateFavoriteMovie(movie.id, isFavorite)

    override fun getMovieDataSourceFactory(searchParams: SearchParams): DataSource<Int, Movie> {
        return localMovieDataStore.getMovieDataSourceFactory(searchParams)
            .map { movieData -> dataEntityMapper.mapFrom(movieData) }
    }

    override fun getFavoriteMovieDataSourceFactory(): DataSource<Int, Movie> {
        return localMovieDataStore.getFavoriteMovieDataSourceFactory()
            .map { movieData -> dataEntityMapper.mapFrom(movieData) }
    }

    override fun loadMovieFromRemote(searchParams: SearchParams): Single<DataEntity<MoviesPage>> {
        return remoteMovieDataStore.findMoviesBySearchParams(searchParams)
            .flatMap<DataEntity<MoviesPage>> { moviesPageData ->
                localMovieDataStore.saveMoviesData(moviesPageData.movies)
                    .andThen(Single.just(DataEntity.Success(pageEntityMapper.mapFrom(moviesPageData))))
            }.onErrorResumeNext { error ->
                Single.just(
                DataEntity.Error(
                    Error(error.message),
                    MoviesPage.EMPTY
                )
            )
        }
    }
}