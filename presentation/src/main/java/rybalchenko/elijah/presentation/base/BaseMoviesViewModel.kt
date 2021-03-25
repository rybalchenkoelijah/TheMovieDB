package rybalchenko.elijah.presentation.base

import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import rybalchenko.elijah.domain.entity.DataState
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.usecase.FavoriteMovieDataSourceUseCase
import rybalchenko.elijah.presentation.utils.data.MovieDataSource
import rybalchenko.elijah.presentation.utils.rx.AppSchedulers
import rybalchenko.elijah.presentation.utils.rx.with

abstract class BaseMoviesViewModel constructor(
    protected val schedulers: AppSchedulers,
    private val favoriteMovieDataSourceUseCase: FavoriteMovieDataSourceUseCase,
    moviePagedConfig: PagedList.Config
) : BaseViewModel() {
    protected val dataState = BehaviorSubject.create<DataState>()
    abstract val movieDataSourceFactory: DataSource.Factory<Int, Movie>
    abstract val boundaryCallback: PagedList.BoundaryCallback<Movie>
    val movieDataSource: Observable<MovieDataSource> by lazy {
        RxPagedListBuilder<Int, Movie>(movieDataSourceFactory, moviePagedConfig)
            .setBoundaryCallback(boundaryCallback)
            .buildObservable()
            .doOnNext { list -> refresh = { list.dataSource.invalidate() } }
            .map { list -> MovieDataSource(list, dataState.hide()) }
    }
    var refresh: () -> Unit = {}

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
}