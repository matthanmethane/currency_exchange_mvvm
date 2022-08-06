package com.dongwoo.currency_exchange.main

import com.dongwoo.currency_exchange.data.CurrencyApi
import com.dongwoo.currency_exchange.data.models.PairConversionRate
import com.dongwoo.currency_exchange.data.models.SupportedCountriesResponse
import com.dongwoo.currency_exchange.util.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
   private val api: CurrencyApi
): MainRepository {
    override suspend fun getConversionRate(
        fromCurrency: String,
        toCurrency: String
    ): Resource<PairConversionRate> {
        return try {
            val response = api.getRate(fromCurrency,toCurrency)
            val result = response.body()
            if(response.isSuccessful && result!=null){
                Resource.Success(result)
            }else{
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Error(e.message?:"An Error has occurred!")
        }
    }

    override suspend fun getCountriesCodes(): Resource<SupportedCountriesResponse> {
        return try {
            val response = api.getCountriesCodes()
            val result = response.body()
            if(response.isSuccessful && result!=null){
                Resource.Success(result)
            }else{
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Error(e.message?:"An Error has occurred!")
        }
    }

}