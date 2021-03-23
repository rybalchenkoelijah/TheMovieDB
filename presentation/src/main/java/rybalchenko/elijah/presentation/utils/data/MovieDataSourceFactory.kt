package rybalchenko.elijah.presentation.utils.data

import androidx.paging.DataSource
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.usecase.FindMoviesUseCase
import rybalchenko.elijah.presentation.utils.rx.AppSchedulers
import javax.inject.Inject

class MovieDataSourceFactory @Inject constructor(
    private val schedulers: AppSchedulers,
    private val useCase: FindMoviesUseCase
) : DataSource.Factory<Int, Movie>() {
    private val compositeDisposable = CompositeDisposable()
    private val _dataSource = BehaviorSubject.create<MovieDataSource>()
    val dataSource: Observable<MovieDataSource> = _dataSource.hide()

    override fun create(): DataSource<Int, Movie> =
        MovieDataSource(schedulers, useCase, compositeDisposable).apply { _dataSource.onNext(this) }

    fun clear() {
        compositeDisposable.clear()
    }
}