package rybalchenko.elijah.data.db

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateConverter @Inject constructor() {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @TypeConverter
    fun fromStringToDate(value: String): Date {
        return try {
            dateFormat.parse(value)
        } catch (throwable: Throwable) {
            null
        } ?: Date()
    }

    @TypeConverter
    fun dateToString(date: Date): String {
        return dateFormat.format(date)
    }
}