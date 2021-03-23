package rybalchenko.elijah.domain

import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.entity.MoviesPage
import kotlin.random.Random


internal val dummyMoviesList: List<Movie> = List(10) { id ->
    val words = listOf("Foo", "Buzz")
    Movie(
        id = id,
        title = words.random().repeat(Random.nextInt(4)) + words.random().repeat(Random.nextInt(4)),
        description = words.random().repeat(Random.nextInt(4)) + words.random()
            .repeat(Random.nextInt(4)),
        posterPath = "/" + words.random().repeat(Random.nextInt(4)) + words.random()
    )
}


internal val dummyMoviesPageResult: MoviesPage = MoviesPage(
    page = 1,
    totalPages = 1,
    totalCount = dummyMoviesList.size,
    movies = dummyMoviesList
)

