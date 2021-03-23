package rybalchenko.elijah.domain.entity

sealed class DataEntity<RequestData>(val data: RequestData) {
    class Error<RequestData>(val error: rybalchenko.elijah.domain.entity.Error, data: RequestData): DataEntity<RequestData>(data)
    class Success<RequestData>(data: RequestData): DataEntity<RequestData>(data)
}