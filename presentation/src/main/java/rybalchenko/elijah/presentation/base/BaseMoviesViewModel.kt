package rybalchenko.elijah.presentation.base

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import rybalchenko.elijah.domain.entity.DataState
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.usecase.FavoriteMovieDataSourceUseCase
import rybalchenko.elijah.presentation.utils.rx.AppSchedulers
import rybalchenko.elijah.presentation.utils.rx.with

abstract class BaseMoviesViewModel constructor(
    protected val schedulers: AppSchedulers,
    private val favoriteMovieDataSourceUseCase: FavoriteMovieDataSourceUseCase,
    moviePagedConfig: PagedList.Config
) : BaseViewModel() {
    protected val _dataState = BehaviorSubject.create<DataState>()
    val dataState: Observable<DataState> = _dataState.hide()
    abstract val movieDataSourceFactory: DataSource.Factory<Int, Movie>
    abstract val boundaryCallback: PagedList.BoundaryCallback<Movie>
    val movieList: LiveData<PagedList<Movie>> by lazy {
        LivePagedListBuilder<Int, Movie>(
            movieDataSourceFactory,
            moviePagedConfig
        ).setBoundaryCallback(boundaryCallback).build()
    }

    fun removeFromFavorite(movie: Movie) {
        favoriteMovieDataSourceUseCase.updateFavoriteMovie(movie, false)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe() with disposable
    }

    fun addToFavorite(movie: Movie) {
        favoriteMovieDataSourceUseCase.updateFavoriteMovie(movie, true)
            .subscribeOn(schedulers.io())
            .subscribe() with disposable
    }

    fun refresh() {
        movieList.value?.dataSource?.invalidate()
    }
}