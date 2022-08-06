package com.dongwoo.currency_exchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dongwoo.currency_exchange.R
import com.dongwoo.currency_exchange.databinding.ActivityMainBinding
import com.dongwoo.currency_exchange.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etCurrencyOne.setText(getString(R.string.parse_float,0.00))

        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.currencyList.collect{ event->
                    when(event) {
                        is MainViewModel.CurrencyEvent.Success<*> -> {
                            val countryCodeListAdapter = ArrayAdapter(
                                this@MainActivity,
                                android.R.layout.simple_spinner_item,
                                event.result as Array<*>)
                            binding.spCurrencyOne.adapter = countryCodeListAdapter
                            binding.spCurrencyTwo.adapter = countryCodeListAdapter
                        }
                        is MainViewModel.CurrencyEvent.Failure -> {
                            Toast.makeText(this@MainActivity, event.errorText, Toast.LENGTH_SHORT).show()
                        }
                        is MainViewModel.CurrencyEvent.Loading -> {
                            Toast.makeText(this@MainActivity, "Getting available currencies...", Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
            launch {
                viewModel.currencyTwoVal.collect{ event->
                    when(event) {
                        is MainViewModel.CurrencyEvent.Success<*> -> {
                            binding.tvLoading.setText(R.string.loading_default)
                            binding.etCurrencyTwo.setText(
                                getString(R.string.parse_float,event.result as Double)
                            )
                        }
                        is MainViewModel.CurrencyEvent.Failure -> {
                            binding.tvLoading.setText(R.string.loading_default)
                            Toast.makeText(this@MainActivity, event.errorText, Toast.LENGTH_SHORT).show()
                        }
                        is MainViewModel.CurrencyEvent.Loading -> {
                            binding.tvLoading.setText(R.string.loading_progress)
                        }
                        else -> Unit
                    }
                }
            }
            viewModel.getCurrencyList()
        }

        binding.spCurrencyOne.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                lifecycleScope.launch{
                    viewModel.getConversionRate(
                        binding.spCurrencyOne.selectedItem as MainViewModel.CurrencySymbol,
                        binding.spCurrencyTwo.selectedItem as MainViewModel.CurrencySymbol
                    )
                    viewModel.getConvertedAmount(binding.etCurrencyOne.text.toString())
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }
        binding.spCurrencyTwo.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                lifecycleScope.launch{
                    viewModel.getConversionRate(
                        binding.spCurrencyOne.selectedItem as MainViewModel.CurrencySymbol,
                        binding.spCurrencyTwo.selectedItem as MainViewModel.CurrencySymbol
                    )
                    viewModel.getConvertedAmount(binding.etCurrencyOne.text.toString())
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        binding.etCurrencyOne.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.getConvertedAmount(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

    }
}