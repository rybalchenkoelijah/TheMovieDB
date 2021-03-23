package rybalchenko.elijah.presentation.di.module

import android.app.Application
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import rybalchenko.elijah.data.db.MovieDao
import rybalchenko.elijah.data.db.MovieDb
import rybalchenko.elijah.data.entity.*
import rybalchenko.elijah.data.repository.MovieRepositoryImpl
import rybalchenko.elijah.domain.common.ListMapper
import rybalchenko.elijah.domain.common.Mapper
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.entity.MoviesPage
import rybalchenko.elijah.domain.repository.MovieRepository
import rybalchenko.elijah.presentation.utils.data.FavoriteMovieDataSourceFactory
import rybalchenko.elijah.presentation.utils.data.MovieDataSourceFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
interface MovieDataModule {
    @Binds
    @Singleton
    fun provideMovieRepository(repository: MovieRepositoryImpl): MovieRepository

    @Binds
    @Singleton
    fun provideMovieDataEntityMapper(mapper: MovieDataEntityMapper): Mapper<MovieData, Movie>

    @Binds
    @Singleton
    fun provideMovieListPageMapper(mapper: MovieListPageMapper): ListMapper<MovieData, MoviesPageData>

    @Binds
    @Singleton
    fun provideMoviePageEntityMapper(mapper: MoviePageDataEntityMapper): Mapper<MoviesPageData, MoviesPage>

    @Binds
    @Singleton
    fun provideMovieDataSourceFactory(factory: MovieDataSourceFactory): DataSource.Factory<Int, Movie>

    @Binds
    @Singleton
    fun provideFavoriteMovieDataSourceFactory(factory: FavoriteMovieDataSourceFactory): DataSource.Factory<Int, Movie>

    companion object {
        @Provides
        @Singleton
        @Named("apiKey")
        fun provideApiKey(): String = "84d185074c12732333fc12b62636b982"

        @Provides
        @Singleton
        fun provideMovieDb(application: Application): MovieDb =
            Room.databaseBuilder(application, MovieDb::class.java, "movies.db").build()

        @Provides
        @Singleton
        fun provideMovieDao(database: MovieDb): MovieDao = database.getMovieDao()

        @Provides
        @Singleton
        fun provideMoviePagedConfig(): PagedList.Config = PagedList.Config.Builder()
            .setPageSize(20)
            .setInitialLoadSizeHint(40)
            .setEnablePlaceholders(true)
            .build()
    }
}