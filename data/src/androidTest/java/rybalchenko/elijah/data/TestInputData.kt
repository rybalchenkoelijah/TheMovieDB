package rybalchenko.elijah.data

import rybalchenko.elijah.data.entity.MovieData
import rybalchenko.elijah.data.entity.MoviesPageData
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random


internal val dummyMoviesList: List<MovieData> = List(10) { id ->
    val words = listOf("Foo", "Buzz")
    MovieData(
        id = id,
        originalTitle = words.random().repeat(Random.nextInt(4)) + words.random()
            .repeat(Random.nextInt(4)),
        title = words.random().repeat(Random.nextInt(4)) + words.random().repeat(Random.nextInt(4)),
        overview = words.random().repeat(Random.nextInt(4)) + words.random()
            .repeat(Random.nextInt(4)),
        releaseDate = Date(
            System.currentTimeMillis() - Random.nextInt(14) * TimeUnit.DAYS.toMillis(
                1
            )
        ),
        posterPath = "/" + words.random().repeat(Random.nextInt(4)) + words.random(),
        isFavorite = false
    )
}


internal val dummyMoviePageDataResult: MoviesPageData = MoviesPageData(
    page = 1,
    totalPages = 1,
    totalResult = dummyMoviesList.size,
    movies = dummyMoviesList
)

