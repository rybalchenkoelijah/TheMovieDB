package rybalchenko.elijah.domain.common

interface ListMapper<in T, out E> {
    fun mapFromList(from: List<T>): E
}