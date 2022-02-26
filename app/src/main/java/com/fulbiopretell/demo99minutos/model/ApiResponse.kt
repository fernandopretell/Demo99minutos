package com.fulbiopretell.demo99minutos.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("code")
    var code: String? = null,
    @SerializedName("data")
    var data: Any? = null
)
