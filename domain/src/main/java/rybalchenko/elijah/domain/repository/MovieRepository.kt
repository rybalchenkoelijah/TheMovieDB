package rybalchenko.elijah.domain.repository

import androidx.paging.DataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import rybalchenko.elijah.domain.entity.DataEntity
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.entity.MoviesPage
import rybalchenko.elijah.domain.entity.SearchParams


interface MovieRepository {
    fun updateFavoriteMovie(movie: Movie, isFavorite: Boolean): Completable
    fun getMovieDataSourceFactory(searchParams: SearchParams): DataSource<Int, Movie>
    fun getFavoriteMovieDataSourceFactory(): DataSource<Int, Movie>
    fun loadMovieFromRemote(searchParams: SearchParams): Single<DataEntity<MoviesPage>>
}