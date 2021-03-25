package rybalchenko.elijah.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import androidx.paging.RxPagedListBuilder
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.*
import org.junit.runner.RunWith
import rybalchenko.elijah.data.db.MovieDao
import rybalchenko.elijah.data.db.MovieDb
import rybalchenko.elijah.data.dummyMoviesList
import rybalchenko.elijah.data.entity.MovieData
import rybalchenko.elijah.domain.entity.SearchParams
import java.io.IOException
import kotlin.random.Random

@RunWith(AndroidJUnit4ClassRunner::class)
class LocalMovieDataStoreTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: MovieDb
    private lateinit var movieDao: MovieDao
    private lateinit var localStore: LocalMovieDataStore

    private val searchParams = SearchParams.createTwoWeeksSearchParams()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MovieDb::class.java)
            .allowMainThreadQueries()
            .build()
        movieDao = db.getMovieDao()
        localStore = LocalMovieDataStore(db.getMovieDao())
    }

    @After
    @Throws(IOException::class)
    fun end() {
        db.close()
    }

    @Test
    fun saveMoviesInLocalStore() {
        localStore.saveMoviesData(dummyMoviesList).blockingAwait()
        val ids = dummyMoviesList.map(MovieData::id)
        val movies = movieDao.loadById(ids)
            .test()
        movies.assertValue { list -> list.all { movieData -> movieData.id in ids } }
    }

    @Test
    fun updateFavoriteMovieInLocalStore() {
        val id = dummyMoviesList.random().id
        val isFavorite = Random.nextBoolean()
        movieDao.insertMovies(dummyMoviesList).blockingAwait()
        localStore.updateFavoriteMovie(id, isFavorite).blockingAwait()
        val movies = movieDao.loadById(listOf(id))
            .test()
        movies.assertValue { list -> list.first().isFavorite == isFavorite }
    }

    @Test
    fun getAndUpdateMoviesInLocalStore() {
        val dataSourceFactory = object : DataSource.Factory<Int, MovieData>() {
            override fun create(): DataSource<Int, MovieData> {
                return localStore.getMovieDataSourceFactory(searchParams)
            }
        }
        val movies = RxPagedListBuilder<Int, MovieData>(dataSourceFactory, 20)
            .buildObservable()
            .test()
        movies.assertValue { list -> list.size == 0 }
        movieDao.insertMovies(dummyMoviesList).blockingAwait()
        movies.assertValueAt(1) { list -> list.size == dummyMoviesList.size }
    }

    @Test
    fun getAndUpdateFavoritesMoviesInLocalStore() {
        val dataSourceFactory = object : DataSource.Factory<Int, MovieData>() {
            override fun create(): DataSource<Int, MovieData> {
                return localStore.getFavoriteMovieDataSourceFactory()
            }
        }
        val movies = RxPagedListBuilder<Int, MovieData>(dataSourceFactory, 20)
            .buildObservable()
            .test()
        movies.assertValue { list -> list.size == 0 }
        movieDao.insertMovies(dummyMoviesList).blockingAwait()
        var count = 0
        repeat(Random.nextInt(dummyMoviesList.size)){ id ->
            localStore.updateFavoriteMovie(dummyMoviesList[id].id, true).blockingAwait()
            count += 1
        }
        movies.assertValueAt(count + 1) { list -> list.size == count }
    }

}