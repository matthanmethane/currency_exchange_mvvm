package com.dongwoo.currency_exchange.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dongwoo.currency_exchange.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
): ViewModel() {
    private lateinit var currencyArrayList: Array<String>

    sealed class CurrencyEvent{
        class Success<T>(val result: T): CurrencyEvent()
        class Failure(val errorText: String): CurrencyEvent()
        object Loading: CurrencyEvent()
        object Empty: CurrencyEvent()

    }

    data class CurrencySymbol(val code: String, val description: String){
        override fun toString(): String {
            return "$code ($description)"
        }
    }

//    private val _currencyList = MutableStateFlow<Array<CurrencySymbol>>(emptyArray())
//    val currencyList: StateFlow<Array<CurrencySymbol>> = _currencyList
    private val _currencyList = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val currencyList: StateFlow<CurrencyEvent> = _currencyList

    private val _currencyTwoVal = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val currencyTwoVal: StateFlow<CurrencyEvent> = _currencyTwoVal

    private var conversionRate: Double = 1.00

    suspend fun getCurrencyList(){
        _currencyList.value = CurrencyEvent.Loading
        val data = repository.getCountriesCodes().data
        if(data==null){
            _currencyList.value = CurrencyEvent.Failure("Failed to get currency list! Check your Internet connection.")
            return
        }
        val currencyArrayList = mutableListOf<CurrencySymbol>()
        data.supportedCodes.forEach { codeCountryList->
            currencyArrayList.add(CurrencySymbol(codeCountryList[0],codeCountryList[1]))
        }
        _currencyList.value = CurrencyEvent.Success(currencyArrayList.toTypedArray())
    }

    suspend fun getConversionRate(fromCurrency: CurrencySymbol, toCurrency: CurrencySymbol){
        val data = repository.getConversionRate(fromCurrency.code,toCurrency.code).data
        if (data==null){
            _currencyTwoVal.value = CurrencyEvent.Failure("Failed to get conversion rate! Check your Internet connection")
            return
        }
        conversionRate = data.conversionRate.toString().toDouble()
    }

    fun getConvertedAmount(amount: String){
        _currencyTwoVal.value = CurrencyEvent.Loading
        val fromCurrencyAmt = amount.toDoubleOrNull()
        if(fromCurrencyAmt==null){
            _currencyTwoVal.value = CurrencyEvent.Failure("Not a float!")
            return
        }
        val result = conversionRate* fromCurrencyAmt
        _currencyTwoVal.value = CurrencyEvent.Success(result)
    }

}