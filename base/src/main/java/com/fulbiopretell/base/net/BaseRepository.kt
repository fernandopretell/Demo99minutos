package com.fulbiopretell.base.net

import com.google.gson.Gson
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

/**
 * Created by Fernando Pretell on 25/02/2022.
 * fernandopretell93@gmail.com
 *
 * Lima, Peru.
 **/

open class BaseRepository {

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): T? {

        val result: Result<T> = safeApiResult(call, errorMessage)
        var data: T? = null

        when (result) {
            is Result.Success -> {
                data = result.data
                Timber.e("Success -> ${Gson().toJson(data)}")
            }

            is Result.Error -> {
                Timber.e("DataRepository $errorMessage & Exception - ${result.exception}")
            }
        }
        return data
    }

    private suspend fun <T : Any> safeApiResult(call: suspend () -> Response<T>, errorMessage: String): Result<T> {
        val response = call.invoke()
        if (response.isSuccessful) return Result.Success(response.body()!!)
        Timber.e(response.body().toString())
        return Result.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
    }
}