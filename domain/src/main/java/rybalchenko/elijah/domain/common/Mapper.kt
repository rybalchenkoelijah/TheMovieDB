package rybalchenko.elijah.domain.common

interface Mapper<in T, out E>{
    fun mapFrom(from: T): E

}