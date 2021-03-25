package rybalchenko.elijah.data.entity

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.*
import org.junit.runner.RunWith
import rybalchenko.elijah.data.dummyMoviePageDataResult

@RunWith(AndroidJUnit4ClassRunner::class)
class MoviePageDataTest {

    private val dataEntityMapper = MovieDataEntityMapper()
    private val pageDataEntityMapper = MoviePageDataEntityMapper(dataEntityMapper)

    @Test
    fun mapMoviePageDataToMoviePage() {
        val item = dummyMoviePageDataResult
        val mapped = pageDataEntityMapper.mapFrom(item)
        Assert.assertTrue(item.page == mapped.page)
        Assert.assertTrue(item.totalPages == mapped.totalPages)
        Assert.assertTrue(item.totalResult == mapped.totalCount)
        item.movies.forEachIndexed { index, item ->
            val mapped = mapped.movies[index]
            Assert.assertTrue(item.id == mapped.id)
            Assert.assertTrue(item.title == mapped.title)
            Assert.assertTrue(item.overview == mapped.description)
            Assert.assertTrue(item.posterPath == mapped.posterPath)
        }
    }
}