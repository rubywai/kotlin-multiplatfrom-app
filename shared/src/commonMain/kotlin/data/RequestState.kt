

import kotlinx.serialization.Serializable

@Serializable
sealed class RequestState {
    @Serializable
    data object Idle : RequestState()

    @Serializable
    data object Loading : RequestState()

    @Serializable
    data class Success(val data: Products) : RequestState()

    @Serializable
    data class Error(val message: String) : RequestState()

    fun getProducts(): Products = (this as Success).data

}
