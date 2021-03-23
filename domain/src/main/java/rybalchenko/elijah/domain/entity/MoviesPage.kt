package rybalchenko.elijah.domain.entity

data class MoviesPage(
    var page: Int,
    val totalPages: Int,
    val totalCount: Int,
    val movies: List<Movie>
){
    companion object{
        val EMPTY = MoviesPage(1,0,0, emptyList())
    }
}