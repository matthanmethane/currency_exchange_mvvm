package com.dongwoo.currency_exchange.data.models


import com.google.gson.annotations.SerializedName

data class SupportedCountriesResponse(
    @SerializedName("documentation")
    val documentation: String,
    @SerializedName("result")
    val result: String,
    @SerializedName("supported_codes")
    val supportedCodes: List<List<String>>,
    @SerializedName("terms_of_use")
    val termsOfUse: String
)