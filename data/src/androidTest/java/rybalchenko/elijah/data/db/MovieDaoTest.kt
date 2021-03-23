package rybalchenko.elijah.data.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.*
import org.junit.runner.RunWith
import rybalchenko.elijah.data.dummyMoviesList
import rybalchenko.elijah.data.entity.MovieData
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random


@RunWith(AndroidJUnit4ClassRunner::class)
class MovieDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: MovieDb
    private lateinit var movieDao: MovieDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MovieDb::class.java).allowMainThreadQueries()
            .build()
        movieDao = db.getMovieDao()
    }

    @After
    @Throws(IOException::class)
    fun end() {
        db.close()
    }

    @Test
    fun addMoviesInDatabase() {
        movieDao.insertMovies(dummyMoviesList).blockingAwait()
        val ids = dummyMoviesList.map(MovieData::id)
        val movies = movieDao.loadById(ids)
            .test()
        movies.assertValue { list -> list.all { movieData -> movieData.id in ids } }
    }

    @Test
    fun updateFavoriteMovieInDatabase() {
        val id = dummyMoviesList.random().id
        val isFavorite = Random.nextBoolean()
        movieDao.insertMovies(dummyMoviesList).blockingAwait()
        movieDao.setFavoriteById(id, isFavorite).blockingAwait()
        val movies = movieDao.loadById(listOf(id))
            .test()
        movies.assertValue { list -> list.first().isFavorite == isFavorite }
    }

    @Test
    fun findMoviesBySearchParams() {
        val startPeriod = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(14))
        val endPeriod = Date(System.currentTimeMillis())
        movieDao.insertMovies(dummyMoviesList).blockingAwait()
        val dataSourceFactory = object : DataSource.Factory<Int, MovieData>() {
            override fun create(): DataSource<Int, MovieData> {
                return movieDao.getMoviesDataSource(startPeriod, endPeriod)
            }
        }
        val movies = RxPagedListBuilder<Int, MovieData>(dataSourceFactory, 20)
            .buildObservable()
            .test()
        movies.assertValue { list -> list.size == dummyMoviesList.size }
    }

    @Test
    fun findFavoriteMovies() {
        val newList = dummyMoviesList.map { movieData -> movieData.copy(isFavorite = Random.nextBoolean()) }
        val count = newList.count { movieData -> movieData.isFavorite }
        movieDao.insertMovies(newList).blockingAwait()
        val dataSourceFactory = object : DataSource.Factory<Int, MovieData>() {
            override fun create(): DataSource<Int, MovieData> {
                return movieDao.getFavoriteMoviesDataSource()
            }
        }
        val movies = RxPagedListBuilder<Int, MovieData>(dataSourceFactory, 20)
            .buildObservable()
            .test()
        movies.assertValue { list -> list.size == count }
    }

}
