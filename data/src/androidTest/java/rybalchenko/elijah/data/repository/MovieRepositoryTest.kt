package rybalchenko.elijah.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import androidx.paging.RxPagedListBuilder
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import org.junit.*
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rybalchenko.elijah.data.api.MovieApi
import rybalchenko.elijah.data.db.DateConverter
import rybalchenko.elijah.data.db.MovieDb
import rybalchenko.elijah.data.dummyMoviePageDataResult
import rybalchenko.elijah.data.dummyMoviesList
import rybalchenko.elijah.data.entity.MovieDataEntityMapper
import rybalchenko.elijah.data.entity.MoviePageDataEntityMapper
import rybalchenko.elijah.domain.entity.DataEntity
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.entity.SearchParams

import rybalchenko.elijah.domain.repository.MovieRepository
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class MovieRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockWebServer = MockWebServer()
    private lateinit var db: MovieDb
    private lateinit var repository: MovieRepository
    private lateinit var localStore: LocalMovieDataStore
    private lateinit var remoteStore: RemoteMovieDataStore

    private val dataEntityMapper = MovieDataEntityMapper()
    private val pageDataEntityMapper = MoviePageDataEntityMapper(dataEntityMapper)
    private val dateConvertor = DateConverter()
    private val gson = GsonBuilder().create()
    private val httpLoggingInterceptor = HttpLoggingInterceptor()
    private val okHttpClient =
        OkHttpClient.Builder().addNetworkInterceptor(httpLoggingInterceptor).build()
    private val searchParams = SearchParams.createTwoWeeksSearchParams()

    @Before
    fun setup() {
        mockWebServer.start()
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MovieDb::class.java)
            .allowMainThreadQueries()
            .build()

        val apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("").toString())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(MovieApi::class.java)

        remoteStore = RemoteMovieDataStore("", dateConvertor, apiService)
        localStore = LocalMovieDataStore(db.getMovieDao())
        repository =
            MovieRepositoryImpl(localStore, remoteStore, pageDataEntityMapper, dataEntityMapper)
    }

    @After
    @Throws(IOException::class)
    fun end() {
        mockWebServer.shutdown()
        db.close()
    }

    @Test
    fun httpOkRequestAndSaveResult() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val input = context.assets.open("okResponse")
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(Buffer().readFrom(input))
        })
        input.close()
        val response = repository.loadMovieFromRemote(searchParams).test()
        response.awaitTerminalEvent()
        response.assertValue { value ->
            value is DataEntity.Success
        }
        val dataSourceFactory = object : DataSource.Factory<Int, Movie>() {
            override fun create(): DataSource<Int, Movie> {
                return repository.getMovieDataSourceFactory(searchParams)
            }
        }
        val movies = RxPagedListBuilder<Int, Movie>(dataSourceFactory, 20)
            .buildObservable()
            .test()
        movies.assertValueAt(0) { list -> list.size == 20 }
    }

    @Test
    fun httpOkRequestThenHttpFailedAndSaveResult() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val ok = context.assets.open("okResponse")
        val failed = context.assets.open("failedResponse")

        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(Buffer().readFrom(ok))
        })

        mockWebServer.enqueue(MockResponse().apply {
            setBody(Buffer().readFrom(failed))
            setResponseCode(422)
        })

        val response = repository.loadMovieFromRemote(searchParams).test()
        response.awaitTerminalEvent()
        val responseTwo = repository.loadMovieFromRemote(searchParams).test()
        responseTwo.awaitTerminalEvent()
        response.assertValue { value -> value is DataEntity.Success }
        response.assertValue { value -> value.data.movies.isNotEmpty() }
        responseTwo.assertValue { value -> value is DataEntity.Error }
        val dataSourceFactory = object : DataSource.Factory<Int, Movie>() {
            override fun create(): DataSource<Int, Movie> {
                return repository.getMovieDataSourceFactory(searchParams)
            }
        }
        val movies = RxPagedListBuilder<Int, Movie>(dataSourceFactory, 20)
            .buildObservable()
            .test()
        movies.assertValueAt(0) { list -> list.size == 20 }
    }

    @Test
    fun httpFailedRequest() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val input = context.assets.open("failedResponse")


        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(422)
            setBody(Buffer().readFrom(input))
        })
        input.close()
        val response = repository.loadMovieFromRemote(searchParams).test()
        response.awaitTerminalEvent()
        response.assertValue { value ->
            value is DataEntity.Error
        }
        val dataSourceFactory = object : DataSource.Factory<Int, Movie>() {
            override fun create(): DataSource<Int, Movie> {
                return repository.getMovieDataSourceFactory(searchParams)
            }
        }
        val movies = RxPagedListBuilder<Int, Movie>(dataSourceFactory, 20)
            .buildObservable()
            .test()
        movies.assertValueAt(0) { list -> list.isEmpty() }
    }

    @Test
    fun mapMovieDataToMovie() {
        val item = dummyMoviesList.random()
        val mapped = dataEntityMapper.mapFrom(item)
        Assert.assertTrue(item.id == mapped.id)
        Assert.assertTrue(item.title == mapped.title)
        Assert.assertTrue(item.overview == mapped.description)
        Assert.assertTrue(item.posterPath == mapped.posterPath)
    }

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
