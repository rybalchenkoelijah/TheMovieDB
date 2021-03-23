package rybalchenko.elijah.test.model.injection.module

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rybalchenko.elijah.test.BuildConfig
import rybalchenko.elijah.test.model.server.MovieAPI
import rybalchenko.elijah.test.model.utils.rx.AppSchedulers
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object NetworkModule {

    private const val CACHE_SIZE = 10L * 1024L * 1024L
    private const val CACHE_DIR_NAME = "Cache"

    private const val CLIENT_CONNECT_TIMEOUT_SECONDS = 30L
    private const val CLIENT_READ_TIMEOUT_SECONDS = 30L
    private const val CLIENT_WRITE_TIMEOUT_SECONDS = 10L

    private const val SERVER_URL = "https://api.themoviedb.org/3/discover/"

    @Provides
    @Singleton
    fun provideCache(context: Context): Cache {
        return Cache(File(context.cacheDir, CACHE_DIR_NAME), CACHE_SIZE)
    }

    @Provides
    @Singleton
    fun provideStethoInterceptor() = StethoInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: Cache,
        stethoInterceptor: StethoInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            clientBuilder
                .addNetworkInterceptor(stethoInterceptor)
                .addNetworkInterceptor(httpLoggingInterceptor)
        }
        clientBuilder
            .cache(cache)
            .readTimeout(CLIENT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(CLIENT_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(CLIENT_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)

        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofit(schedulers: AppSchedulers, okHttpClient: OkHttpClient, gson: Gson) =
        Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideMovieAPI(retrofit: Retrofit) = retrofit.create(MovieAPI::class.java)
}