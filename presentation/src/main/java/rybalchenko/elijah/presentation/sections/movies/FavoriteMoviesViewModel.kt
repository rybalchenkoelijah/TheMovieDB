package rybalchenko.elijah.presentation.sections.movies


import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.subjects.BehaviorSubject
import rybalchenko.elijah.domain.entity.DataState
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.usecase.UpdateMoviesUseCase
import rybalchenko.elijah.presentation.base.BaseMoviesViewModel
import rybalchenko.elijah.presentation.utils.data.FavoriteMovieDataSourceFactory
import rybalchenko.elijah.presentation.utils.rx.AppSchedulers

import javax.inject.Inject

class FavoriteMoviesViewModel @Inject constructor(
    schedulers: AppSchedulers,
    updateMoviesUseCase: UpdateMoviesUseCase,
    moviePagedConfig: PagedList.Config
) : BaseMoviesViewModel<FavoriteMovieDataSourceFactory>(
    schedulers,
    updateMoviesUseCase,
    moviePagedConfig
) {
    private val _dataState = BehaviorSubject.create<DataState>()
    override val dataState = _dataState.hide()

    override val movieList: LiveData<PagedList<Movie>> by lazy {
        LivePagedListBuilder<Int, Movie>(
            movieDataSourceFactory,
            moviePagedConfig
        ).setBoundaryCallback(object : PagedList.BoundaryCallback<Movie>() {
            override fun onZeroItemsLoaded() {
                _dataState.onNext(DataState.EMPTY)
            }

            override fun onItemAtEndLoaded(itemAtEnd: Movie) {
                _dataState.onNext(DataState.SUCCESS)
            }

            override fun onItemAtFrontLoaded(itemAtFront: Movie) {

            }
        }).build()
    }

    @Inject
    override lateinit var movieDataSourceFactory: FavoriteMovieDataSourceFactory

}