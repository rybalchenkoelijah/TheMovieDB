package rybalchenko.elijah.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import io.reactivex.Single
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import rybalchenko.elijah.domain.dummyMoviesList
import rybalchenko.elijah.domain.dummyMoviesPageResult
import rybalchenko.elijah.domain.entity.DataEntity
import rybalchenko.elijah.domain.entity.Error
import rybalchenko.elijah.domain.entity.MoviesPage
import rybalchenko.elijah.domain.entity.SearchParams
import rybalchenko.elijah.domain.repository.MovieRepository


@RunWith(AndroidJUnit4ClassRunner::class)
class MovieDataSourceUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val searchParams = SearchParams.createTwoWeeksSearchParams()

    @Mock
    lateinit var repository: MovieRepository

    lateinit var useCaseTest: MovieDataSourceUseCase

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        useCaseTest = MovieDataSourceUseCaseImpl(repository)
    }

    @Test
    fun loadMoviesPageFromRemote() {
        mockSuccess(dummyMoviesPageResult)
        val result = useCaseTest.loadMovieFromRemote(searchParams).test()
        result.awaitTerminalEvent()
        result.assertValue { entity -> entity is DataEntity.Success }
        result.assertValue { entity -> entity.data.movies.size == dummyMoviesList.size }
    }

    @Test
    fun loadMoviesPageFromRemoteFailed() {
        val error = "TestError"
        mockError(error)
        val result = useCaseTest.loadMovieFromRemote(searchParams).test()
        result.awaitTerminalEvent()
        result.assertValue { entity -> entity is DataEntity.Error && entity.error.message == error }
        result.assertValue { entity -> entity.data == MoviesPage.EMPTY }
    }

    private fun mockSuccess(entity: MoviesPage) {
        Mockito.`when`(repository.loadMovieFromRemote(searchParams))
            .thenReturn(Single.just(DataEntity.Success(entity)))
    }

    private fun mockError(message: String) {
        Mockito.`when`(repository.loadMovieFromRemote(searchParams)).thenReturn(
            Single.just(
                DataEntity.Error(
                    Error(message), MoviesPage.EMPTY
                )
            )
        )
    }

}
