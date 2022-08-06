package com.dongwoo.currency_exchange.data

import com.dongwoo.currency_exchange.data.models.PairConversionRate
import com.dongwoo.currency_exchange.data.models.SupportedCountriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface CurrencyApi {

    @GET("pair/{fromCurrency}/{toCurrency}")
    suspend fun getRate(
        @Path("fromCurrency") fromCurrency: String,
        @Path("toCurrency") toCurrency: String
    ): Response<PairConversionRate>

    @GET("codes")
    suspend fun getCountriesCodes(): Response<SupportedCountriesResponse>

}