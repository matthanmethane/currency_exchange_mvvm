package com.dongwoo.currency_exchange.main

import com.dongwoo.currency_exchange.data.models.PairConversionRate
import com.dongwoo.currency_exchange.data.models.SupportedCountriesResponse
import com.dongwoo.currency_exchange.util.Resource

interface MainRepository {

    suspend fun getConversionRate(
        fromCurrency: String,
        toCurrency: String
    ): Resource<PairConversionRate>

    suspend fun getCountriesCodes(): Resource<SupportedCountriesResponse>


}