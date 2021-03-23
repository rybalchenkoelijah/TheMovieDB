package rybalchenko.elijah.presentation.utils.data

import androidx.paging.PositionalDataSource
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import rybalchenko.elijah.domain.entity.DataEntity
import rybalchenko.elijah.domain.entity.DataState
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.domain.entity.SearchParams
import rybalchenko.elijah.domain.usecase.FindMoviesUseCase
import rybalchenko.elijah.presentation.utils.rx.AppSchedulers
import rybalchenko.elijah.presentation.utils.rx.with

class MovieDataSource(
    private val schedulers: AppSchedulers,
    private val useCase: FindMoviesUseCase,
    private val compositeDisposable: CompositeDisposable
) : PositionalDataSource<Movie>() {
    private val _dataState = BehaviorSubject.create<DataState>()
    val dataState: Observable<DataState> = _dataState.hide()
    var searchParams: SearchParams = SearchParams.createTwoWeeksSearchParams()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Movie>) {
        useCase.invoke(searchParams)
            .doOnSubscribe { _dataState.onNext(DataState.PROGRESS) }
            .take(1)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe { dataEntity ->
                if (dataEntity.data.movies.isEmpty()) {
                    _dataState.onNext(DataState.EMPTY)
                } else {
                    when (dataEntity) {
                        is DataEntity.Error -> _dataState.onNext(DataState.FAILED)
                        is DataEntity.Success -> _dataState.onNext(DataState.SUCCESS)
                    }
                }
                callback.onResult(dataEntity.data.movies)
            } with compositeDisposable
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Movie>) {
        useCase.invoke(searchParams)
            .doOnSubscribe { _dataState.onNext(DataState.PROGRESS) }
            .take(1)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe { dataEntity ->
                val data = dataEntity.data
                when (dataEntity) {
                    is DataEntity.Error -> _dataState.onNext(DataState.FAILED)
                    is DataEntity.Success -> _dataState.onNext(DataState.SUCCESS)
                }
                if (data.movies.isEmpty()) {
                    _dataState.onNext(DataState.EMPTY)
                }
                callback.onResult(
                    dataEntity.data.movies,
                    0,
                    data.totalCount
                )
            } with compositeDisposable
    }
}