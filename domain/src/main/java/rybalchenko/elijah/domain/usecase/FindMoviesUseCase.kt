package rybalchenko.elijah.domain.usecase

import io.reactivex.Observable
import rybalchenko.elijah.domain.entity.DataEntity
import rybalchenko.elijah.domain.entity.MoviesPage
import rybalchenko.elijah.domain.entity.SearchParams
import rybalchenko.elijah.domain.repository.MovieRepository
import javax.inject.Inject

interface FindMoviesUseCase {

    fun invoke(searchParams: SearchParams): Observable<DataEntity<MoviesPage>>
}

class FindAllMoviesUseCase @Inject constructor(private val repository: MovieRepository) :
    FindMoviesUseCase {

    override fun invoke(searchParams: SearchParams): Observable<DataEntity<MoviesPage>> =
        repository.findMoviesBySearchParams(searchParams)
}
