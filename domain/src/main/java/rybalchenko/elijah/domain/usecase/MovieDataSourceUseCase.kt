package rybalchenko.elijah.domain.usecase

import androidx.paging.DataSource
import io.reactivex.Completable
import io.reactivex.Single
import rybalchenko.elijah.domain.entity.DataEntity
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.entity.MoviesPage
import rybalchenko.elijah.domain.entity.SearchParams
import rybalchenko.elijah.domain.repository.MovieRepository
import javax.inject.Inject

interface MovieDataSourceUseCase {
    fun createMovieDataSourceFactory(searchParams: SearchParams): DataSource<Int, Movie>
    fun loadMovieFromRemote(searchParams: SearchParams): Single<DataEntity<MoviesPage>>
}

class MovieDataSourceUseCaseImpl @Inject constructor(val repository: MovieRepository):  MovieDataSourceUseCase{
    override fun createMovieDataSourceFactory(searchParams: SearchParams): DataSource<Int, Movie> {
       return repository.getMovieDataSourceFactory(searchParams)
    }

    override fun loadMovieFromRemote(searchParams: SearchParams): Single<DataEntity<MoviesPage>>{
        return repository.loadMovieFromRemote(searchParams)
    }
}