package rybalchenko.elijah.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import rybalchenko.elijah.domain.common.Mapper
import rybalchenko.elijah.domain.entity.Movie
import java.util.*
import javax.inject.Inject

@Entity(tableName = "movie_data")
data class MovieData(
    @Expose
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int,
    @Expose
    @SerializedName("original_title")
    @ColumnInfo(name = "original_title")
    val originalTitle: String,
    @Expose
    @SerializedName("overview")
    @ColumnInfo(name = "overview")
    val overview: String,
    @Expose
    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,
    @Expose
    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String,
    @Expose
    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    val releaseDate: Date,
    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean = false
)

class MovieDataEntityMapper @Inject constructor() : Mapper<MovieData, Movie> {
    override fun mapFrom(from: MovieData): Movie = Movie(
        id = from.id,
        title = from.title,
        description = from.overview,
        posterPath = from.posterPath ?: "",
        isFavorite = from.isFavorite
    )

}
