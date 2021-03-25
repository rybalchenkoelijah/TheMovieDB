package rybalchenko.elijah.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
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
import rybalchenko.elijah.domain.entity.SearchParams
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class RemoteMovieDataStoreTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockWebServer = MockWebServer()
    private lateinit var db: MovieDb
    private lateinit var remoteStore: RemoteMovieDataStore

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
    }

    @After
    @Throws(IOException::class)
    fun end() {
        mockWebServer.shutdown()
        db.close()
    }

    @Test
    fun findMoviesWithSuccessResponse() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val input = context.assets.open("okResponse")
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(Buffer().readFrom(input))
        })
        input.close()
        val response = remoteStore.findMoviesBySearchParams(searchParams).test()
        response.awaitTerminalEvent()
        response.assertValue { value -> value.movies.size == 20 }
        response.assertValue { value -> value.totalPages == 7 }
        response.assertValue { value -> value.totalResult == 125 }
        response.assertValue { value -> value.page == 2 }
    }

    @Test
    fun findMoviesWithErrorResponse() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val input = context.assets.open("failedResponse")
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(422)
            setBody(Buffer().readFrom(input))
        })
        input.close()
        val response = remoteStore.findMoviesBySearchParams(searchParams).test()
        response.awaitTerminalEvent()
        response.assertError { error -> true }
    }
}