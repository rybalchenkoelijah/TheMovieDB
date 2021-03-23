package rybalchenko.elijah.domain.repository

import io.reactivex.Completable
import io.reactivex.Observable
import rybalchenko.elijah.domain.entity.DataEntity
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.entity.MoviesPage
import rybalchenko.elijah.domain.entity.SearchParams

interface MovieRepository {
    fun findMoviesBySearchParams(searchParams: SearchParams): Observable<DataEntity<MoviesPage>>
    fun updateFavoriteMovie(movie: Movie, isFavorite: Boolean): Completable
}