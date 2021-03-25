package rybalchenko.elijah.presentation.utils.data

import androidx.paging.PagedList
import io.reactivex.Observable
import rybalchenko.elijah.domain.entity.DataState
import rybalchenko.elijah.domain.entity.Movie

class MovieDataSource(val movies: PagedList<Movie>, val dataState: Observable<DataState>)