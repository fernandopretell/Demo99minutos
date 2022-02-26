package com.disnovo.potencie21.source.remote

import com.fulbiopretell.demo99minutos.model.MyPlaces
import com.fulbiopretell.demo99minutos.model.PlaceDetail
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by fernandopretell.
 */
interface WebServiceData {

    //@FormUrlEncoded
    /*POST(Urls.LOGIN)
    fun login(@Body login: Login?): Deferred<Response<ApiResponse?>?>

    @POST(Urls.SETTINGS)
    fun settings(@Body tokenKey: TokenKey?, @HeaderMap headers: Map<String, String>): Deferred<Response<ApiResponse?>?>*/

    @GET
    fun getNearbyPlaces(@Url url:String): Deferred<Response<MyPlaces>>

    @GET
    fun getDetailPlace(@Url url:String): Deferred<PlaceDetail>

    @GET("maps/api/directions/json")
    fun getDirections(@Query("origin") origin:String, @Query("destination") destination:String):Call<String>

}