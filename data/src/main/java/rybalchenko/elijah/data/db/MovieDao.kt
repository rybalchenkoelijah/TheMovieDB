package rybalchenko.elijah.data.db

import androidx.paging.PositionalDataSource
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import rybalchenko.elijah.data.entity.MovieData
import java.util.*

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie_data WHERE isFavorite = 1")
    fun getFavoriteMoviesDataSource(): PositionalDataSource<MovieData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovies(movies: List<MovieData>): Completable

    @Query("UPDATE movie_data SET isFavorite=:isFavorite WHERE id=:id")
    fun setFavoriteById(id: Int, isFavorite: Boolean): Completable

    @Query("SELECT * FROM movie_data WHERE release_date >= :startPeriod AND release_date <= :endPeriod ORDER BY release_date DESC LIMIT :limit OFFSET :offset")
    fun searchMoviesInPeriod(
        startPeriod: Date,
        endPeriod: Date,
        offset: Int,
        limit: Int
    ): Observable<List<MovieData>>
}