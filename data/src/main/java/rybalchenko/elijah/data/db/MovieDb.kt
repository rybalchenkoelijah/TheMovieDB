package rybalchenko.elijah.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import rybalchenko.elijah.data.entity.MovieData

@Database(entities = [MovieData::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class MovieDb: RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}