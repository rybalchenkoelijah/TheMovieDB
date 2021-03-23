package rybalchenko.elijah.presentation.sections.movies

import androidx.paging.PagedList
import io.reactivex.Observable
import rybalchenko.elijah.domain.entity.DataState
import rybalchenko.elijah.domain.usecase.UpdateMoviesUseCase
import rybalchenko.elijah.presentation.base.BaseMoviesViewModel
import rybalchenko.elijah.presentation.utils.data.MovieDataSourceFactory
import rybalchenko.elijah.presentation.utils.rx.AppSchedulers

import javax.inject.Inject

class MoviesViewModel @Inject constructor(
     schedulers: AppSchedulers,
    updateMoviesUseCase: UpdateMoviesUseCase,
    moviePagedConfig: PagedList.Config
) : BaseMoviesViewModel<MovieDataSourceFactory>(schedulers, updateMoviesUseCase, moviePagedConfig) {

    @Inject
    override lateinit var movieDataSourceFactory: MovieDataSourceFactory

    override val dataState: Observable<DataState> by lazy {
        movieDataSourceFactory.dataSource.flatMap { dataSource -> dataSource.dataState }
    }

    override fun onCleared() {
        super.onCleared()
        movieDataSourceFactory.clear()
    }
}