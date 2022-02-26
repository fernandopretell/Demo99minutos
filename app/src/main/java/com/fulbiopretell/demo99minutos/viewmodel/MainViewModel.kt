package com.fulbiopretell.demo99minutos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.disnovo.potencie21.source.remote.HelperWs
import com.fulbiopretell.demo99minutos.model.MyPlaces
import com.fulbiopretell.demo99minutos.repositories.MainRepository
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class MainViewModel : ViewModel() {

    private val repository: MainRepository = MainRepository(HelperWs.getServiceData())

    fun getNearbyPlaces(url: String) = liveData(Dispatchers.IO) {
        emit(ViewState.Loading)
        try {
            val validateGetData = repository.callNearbyPlaces(url) as MainRepository.State.Success
            if (validateGetData.response?.status.equals("OK")) {
                emit(ViewState.GetDataSuccess(validateGetData.response))
            } else {
                emit(ViewState.GetDataResponseFailure(validateGetData.response?.status ?: "ERROR"))
            }

        } catch (e: Exception) {
            emit(ViewState.GetDataResponseFailure(e.message ?: "error"))
            Timber.e(e)
        }
    }

    sealed class ViewState() {
        object Loading : ViewState()
        data class GetDataSuccess(val response: MyPlaces?) : ViewState()
        data class GetDataResponseFailure(val error: String) : ViewState()
    }
}