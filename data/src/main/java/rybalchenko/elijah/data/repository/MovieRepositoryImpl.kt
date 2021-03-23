package rybalchenko.elijah.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function
import rybalchenko.elijah.data.entity.MoviePageDataEntityMapper
import rybalchenko.elijah.domain.entity.*
import rybalchenko.elijah.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val localMovieDataStore: LocalMovieDataStore,
    private val remoteMovieDataStore: RemoteMovieDataStore,
    private val pageEntityMapper: MoviePageDataEntityMapper
) : MovieRepository {
    override fun findMoviesBySearchParams(searchParams: SearchParams): Observable<DataEntity<MoviesPage>> {
        return remoteMovieDataStore.findMoviesBySearchParams(searchParams)
            .flatMap { moviesPageData ->
                localMovieDataStore.saveMoviesData(moviesPageData.movies)
                    .andThen(localMovieDataStore.findMoviesBySearchParams(searchParams))
                    .map<DataEntity<MoviesPage>> { localDataPage ->
                        DataEntity.Success(pageEntityMapper.mapFrom(moviesPageData.copy(movies = localDataPage.movies)))
                    }
            }
            .onErrorResumeNext(Function { error ->
                localMovieDataStore.findMoviesBySearchParams(searchParams)
                    .map<DataEntity<MoviesPage>> { localDataPage ->
                        DataEntity.Error(
                            Error(error.message),
                            pageEntityMapper.mapFrom(localDataPage)
                        )
                    }
            })
    }

    override fun updateFavoriteMovie(movie: Movie, isFavorite: Boolean): Completable =
        localMovieDataStore.updateFavoriteMovie(movie.id, isFavorite)
}