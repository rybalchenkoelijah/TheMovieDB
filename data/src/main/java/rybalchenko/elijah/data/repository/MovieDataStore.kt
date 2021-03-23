package rybalchenko.elijah.data.repository

import io.reactivex.Observable
import rybalchenko.elijah.data.entity.MoviesPageData
import rybalchenko.elijah.domain.entity.SearchParams

interface MovieDataStore {
    fun findMoviesBySearchParams(searchParams: SearchParams) : Observable<MoviesPageData>
}