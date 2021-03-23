package rybalchenko.elijah.data.repository

import io.reactivex.Observable
import rybalchenko.elijah.data.api.MovieApi
import rybalchenko.elijah.data.db.DateConverter
import rybalchenko.elijah.data.entity.MoviesPageData
import rybalchenko.elijah.domain.entity.SearchParams
import javax.inject.Inject
import javax.inject.Named

class RemoteMovieDataStore @Inject constructor(
    @Named("apiKey")
    private val apiKey: String,
    private val converter: DateConverter,
    private val movieApi: MovieApi
) : MovieDataStore {
    override fun findMoviesBySearchParams(searchParams: SearchParams): Observable<MoviesPageData> {
        return movieApi.getMoviesByPeriod(
            apiKey, searchParams.currentPage,
            searchParams.sortBy,
            converter.dateToString(searchParams.startPeriod),
            converter.dateToString(searchParams.endPeriod)
        ).toObservable()
    }
}