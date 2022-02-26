package com.fulbiopretell.demo99minutos.repositories

import com.disnovo.potencie21.source.remote.WebServiceData
import com.fulbiopretell.base.net.BaseRepository
import com.fulbiopretell.demo99minutos.model.MyPlaces
import retrofit2.Response
import timber.log.Timber

class MainRepository (private val api: WebServiceData) : BaseRepository() {

    suspend fun callNearbyPlaces(url: String): State? {

        Timber.e("Request callNearbyPlaces -> " + url)
        val nearbyPlacesResponse = safeApiCall(
            call = { ((api.getNearbyPlaces(url).await() as Response<*>)) },
            errorMessage = "Error Occurred during getting safe Api result"
        )
        return State.Success(nearbyPlacesResponse as MyPlaces?)
    }

    sealed class State {
        data class Success(val response: MyPlaces?) : State()
        data class Failure(val errorMessage: String) : State()
    }
}