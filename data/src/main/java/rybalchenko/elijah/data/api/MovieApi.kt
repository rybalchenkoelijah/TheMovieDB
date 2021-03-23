package rybalchenko.elijah.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import rybalchenko.elijah.data.entity.MoviesPageData

interface MovieApi {

    @GET("discover/movie")
    fun getMoviesByPeriod(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String,
        @Query("primary_release_date.gte") startPeriod: String,
        @Query("primary_release_date.lte") endPeriod: String
    ): Single<MoviesPageData>
}