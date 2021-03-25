package rybalchenko.elijah.data.entity

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.*
import org.junit.runner.RunWith
import rybalchenko.elijah.data.dummyMoviesList

@RunWith(AndroidJUnit4ClassRunner::class)
class MovieDataTest {

    private val dataEntityMapper = MovieDataEntityMapper()

    @Test
    fun mapMovieDataToMovie() {
        val item = dummyMoviesList.random()
        val mapped = dataEntityMapper.mapFrom(item)
        Assert.assertTrue(item.id == mapped.id)
        Assert.assertTrue(item.title == mapped.title)
        Assert.assertTrue(item.overview == mapped.description)
        Assert.assertTrue(item.posterPath == mapped.posterPath)
        Assert.assertTrue(item.isFavorite == mapped.isFavorite)
    }
}