package rybalchenko.elijah.presentation.sections.movies

import androidx.paging.DataSource
import androidx.paging.PagedList
import rybalchenko.elijah.domain.entity.DataEntity
import rybalchenko.elijah.domain.entity.DataState
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.entity.SearchParams
import rybalchenko.elijah.domain.usecase.FavoriteMovieDataSourceUseCase
import rybalchenko.elijah.domain.usecase.MovieDataSourceUseCase
import rybalchenko.elijah.presentation.base.BaseMoviesViewModel
import rybalchenko.elijah.presentation.utils.rx.AppSchedulers
import rybalchenko.elijah.presentation.utils.rx.with

import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    movieDataSourceUseCase: MovieDataSourceUseCase,
    schedulers: AppSchedulers,
    favoriteMovieDataSourceUseCase: FavoriteMovieDataSourceUseCase,
    moviePagedConfig: PagedList.Config
) : BaseMoviesViewModel(
    schedulers,
    favoriteMovieDataSourceUseCase,
    moviePagedConfig
) {
    private var searchParams = SearchParams.createTwoWeeksSearchParams()
    override val movieDataSourceFactory by lazy {
        object : DataSource.Factory<Int, Movie>() {
            override fun create(): DataSource<Int, Movie> {
                return movieDataSourceUseCase.createMovieDataSourceFactory(searchParams)
            }
        }
    }

    override val boundaryCallback = object : PagedList.BoundaryCallback<Movie>() {
        override fun onZeroItemsLoaded() {
            movieDataSourceUseCase.loadMovieFromRemote(searchParams)
                .doOnSubscribe { _dataState.onNext(DataState.PROGRESS) }
                .subscribe({ dataEntity ->
                    when (dataEntity) {
                        is DataEntity.Error -> _dataState.onNext(DataState.FAILED)
                        is DataEntity.Success -> {
                            _dataState.onNext(DataState.SUCCESS)
                            searchParams =
                                searchParams.copy(currentPage = searchParams.currentPage + 1)
                        }
                    }
                    if (dataEntity.data.movies.isEmpty()) {
                        _dataState.onNext(DataState.EMPTY)
                    }
                }, {
                    _dataState.onNext(DataState.FAILED)
                }) with disposable
        }

        override fun onItemAtEndLoaded(itemAtEnd: Movie) {
            movieDataSourceUseCase.loadMovieFromRemote(searchParams)
                .doOnSubscribe { _dataState.onNext(DataState.PROGRESS) }
                .subscribe({ dataEntity ->
                    when (dataEntity) {
                        is DataEntity.Error -> _dataState.onNext(DataState.FAILED)
                        is DataEntity.Success -> {
                            _dataState.onNext(DataState.SUCCESS)
                            searchParams =
                                searchParams.copy(currentPage = searchParams.currentPage + 1)
                        }
                    }
                }, {
                    _dataState.onNext(DataState.FAILED)
                }) with disposable
        }

        override fun onItemAtFrontLoaded(itemAtFront: Movie) {
            _dataState.onNext(DataState.SUCCESS)
        }
    }
}