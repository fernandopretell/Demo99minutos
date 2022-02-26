package com.fulbiopretell.demo99minutos.model
import com.google.gson.annotations.SerializedName


data class MyPlaces(
    @SerializedName("html_attributions")
    val htmlAttributions: List<Any>,
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("status")
    val status: String
)

data class Result(
    @SerializedName("geometry")
    var geometry: Geometry? = null,
    @SerializedName("icon")
    var icon: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("opening_hours")
    var openingHours: OpeningHours? = null,
    @SerializedName("photos")
    var photos: List<Photo>? = null,
    @SerializedName("place_id")
    var placeId: String? = null,
    @SerializedName("plus_code")
    var plusCode: PlusCode? = null,
    @SerializedName("price_level")
    var priceLevel: Int? = null,
    @SerializedName("rating")
    var rating: Double? = null,
    @SerializedName("reference")
    var reference: String? = null,
    @SerializedName("scope")
    var scope: String? = null,
    @SerializedName("types")
    var types: List<String>? = null,
    @SerializedName("user_ratings_total")
    var userRatingsTotal: Int? = null,
    @SerializedName("vicinity")
    var vicinity: String? = null,
    var distanceFromCurrent: Double? = null
)

data class Geometry(
    @SerializedName("location")
    val location: Location,
    @SerializedName("viewport")
    val viewport: Viewport
)

data class Location(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)

data class Viewport(
    @SerializedName("northeast")
    val northeast: Northeast,
    @SerializedName("southwest")
    val southwest: Southwest
)

data class Northeast(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)

data class Southwest(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)

data class OpeningHours(
    @SerializedName("open_now")
    val openNow: Boolean
)

data class Photo(
    @SerializedName("height")
    val height: Int,
    @SerializedName("html_attributions")
    val htmlAttributions: List<String>,
    @SerializedName("photo_reference")
    val photoReference: String,
    @SerializedName("width")
    val width: Int
)

data class PlusCode(
    @SerializedName("compound_code")
    val compoundCode: String,
    @SerializedName("global_code")
    val globalCode: String
)