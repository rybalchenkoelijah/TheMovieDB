package rybalchenko.elijah.presentation.sections.movies


import androidx.paging.DataSource
import androidx.paging.PagedList
import rybalchenko.elijah.domain.entity.DataState
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.usecase.FavoriteMovieDataSourceUseCase
import rybalchenko.elijah.presentation.base.BaseMoviesViewModel
import rybalchenko.elijah.presentation.utils.rx.AppSchedulers

import javax.inject.Inject

class FavoriteMoviesViewModel @Inject constructor(
    schedulers: AppSchedulers,
    favoriteMovieDataSourceUseCase: FavoriteMovieDataSourceUseCase,
    moviePagedConfig: PagedList.Config
) : BaseMoviesViewModel(
    schedulers,
    favoriteMovieDataSourceUseCase,
    moviePagedConfig
) {
    override val movieDataSourceFactory: DataSource.Factory<Int, Movie> =
        object : DataSource.Factory<Int, Movie>() {
            override fun create(): DataSource<Int, Movie> {
                return favoriteMovieDataSourceUseCase.createFavoriteMovieDataSourceFactory()
            }
        }
    override val boundaryCallback: PagedList.BoundaryCallback<Movie> =
        object : PagedList.BoundaryCallback<Movie>() {
            override fun onZeroItemsLoaded() {
                _dataState.onNext(DataState.EMPTY)
            }

            override fun onItemAtEndLoaded(itemAtEnd: Movie) {
                _dataState.onNext(DataState.SUCCESS)
            }
        }

}