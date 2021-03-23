package rybalchenko.elijah.domain.entity

import java.util.*
import java.util.concurrent.TimeUnit

data class SearchParams(
    val startPeriod: Date,
    val endPeriod: Date,
    val currentPage: Int,
    val pageSize: Int,
    val sortBy: String
) {

    companion object {
        fun createTwoWeeksSearchParams(): SearchParams = SearchParams(
            Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(14)),
            Date(System.currentTimeMillis()),
            1,
            20,
            "original_title.desc"
        )
    }
}