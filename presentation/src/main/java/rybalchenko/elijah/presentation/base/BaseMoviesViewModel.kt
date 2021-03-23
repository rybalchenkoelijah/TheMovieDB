package rybalchenko.elijah.presentation.base

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.Observable
import rybalchenko.elijah.domain.entity.DataState
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.usecase.UpdateMoviesUseCase
import rybalchenko.elijah.presentation.utils.rx.AppSchedulers
import rybalchenko.elijah.presentation.utils.rx.with

abstract class BaseMoviesViewModel<DS : DataSource.Factory<Int, Movie>> constructor(
    protected val schedulers: AppSchedulers,
    private val updateMoviesUseCase: UpdateMoviesUseCase,
    moviePagedConfig: PagedList.Config
) : BaseViewModel() {
    abstract var movieDataSourceFactory: DS
    abstract val dataState: Observable<DataState>
    open val movieList: LiveData<PagedList<Movie>> by lazy {
        LivePagedListBuilder<Int, Movie>(
            movieDataSourceFactory,
            moviePagedConfig
        ).build()
    }

    fun removeFromFavorite(movie: Movie, successCallback: () -> Unit = {}) {
        updateMoviesUseCase.invoke(movie, false)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe(successCallback) with disposable
    }

    fun addToFavorite(movie: Movie, successCallback: () -> Unit = {}) {
        updateMoviesUseCase.invoke(movie, true)
            .subscribeOn(schedulers.io())
            .subscribe { successCallback } with disposable
    }

    fun refresh() {
        movieList.value?.dataSource?.invalidate()
    }
}