package rybalchenko.elijah.data.entity


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import rybalchenko.elijah.domain.common.ListMapper
import rybalchenko.elijah.domain.common.Mapper
import rybalchenko.elijah.domain.entity.MoviesPage
import javax.inject.Inject

data class MoviesPageData(
    @Expose
    @SerializedName("page")
    var page: Int,
    @Expose
    @SerializedName("total_pages")
    val totalPages: Int,
    @Expose
    @SerializedName("total_results")
    val totalResult: Int,
    @Expose
    @SerializedName("results")
    val movies: List<MovieData>
)

class MovieListPageMapper @Inject constructor() : ListMapper<MovieData, MoviesPageData> {
    override fun mapFromList(from: List<MovieData>) = MoviesPageData(
        page = 1,
        totalPages = 1,
        totalResult = from.size,
        movies = from
    )
}

class MoviePageDataEntityMapper @Inject constructor(private val moviesDataEntityMapper: MovieDataEntityMapper): Mapper<MoviesPageData, MoviesPage> {
    override fun mapFrom(from: MoviesPageData) = MoviesPage(
        page = from.page,
        totalPages = from.totalPages,
        totalCount = from.totalResult,
        movies = from.movies.map(moviesDataEntityMapper::mapFrom)
    )
}
