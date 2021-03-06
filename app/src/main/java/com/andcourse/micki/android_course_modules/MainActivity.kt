package com.andcourse.micki.android_course_modules

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.math.BigDecimal
import java.net.URL
import java.util.Currency
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var convertButton: Button
    private lateinit var imageView: ImageView
    private lateinit var currencyAmount: EditText
    private lateinit var currencyList: Spinner
    private lateinit var toCurrencyList: Spinner
    private lateinit var textView: TextView


    private var currentConversionRate: Double = 0.0

    private fun extractConversionRate(fromCurrency: String, toCurrency: String): Double {
        val apiURL = URL("http://free.currencyconverterapi.com/api/v5/convert?q=$fromCurrency" + "_" + "$toCurrency&compact=y&apiKey=bd2d9364f4c8337aaf50")

        Volley.newRequestQueue(this).add<String?>(StringRequest(Request.Method.GET, apiURL.toString(), Response.Listener<String> { response ->
            val conversionValue = JSONObject(response)
            Log.i("Info", conversionValue.toString())
            currentConversionRate = BigDecimal((conversionValue.get(conversionValue.names().get(0).toString()) as JSONObject)["val"].toString()).toDouble()
        }, Response.ErrorListener { }))
        return currentConversionRate
    }

    internal fun getCurrenciesList(): List<String> {
        val availableLocales = Locale.getAvailableLocales()
        val currenciesList = mutableListOf<String>()
        for (locale in availableLocales) {
            if (locale.toString() != "") {
                try {
                    currenciesList.add(Currency.getInstance(locale).currencyCode)
                } catch (e: Exception) {
                }
            }
        }
        return currenciesList.sorted().distinct()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        convertButton = findViewById(R.id.button)
        imageView = findViewById(R.id.imageView)
        currencyAmount = findViewById(R.id.editText)
        currencyList = findViewById(R.id.spinner)
        toCurrencyList = findViewById(R.id.spinner2)
        textView = findViewById(R.id.textView)

        val currenciesList = getCurrenciesList()

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, currenciesList)

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        currencyList.adapter = arrayAdapter
        toCurrencyList.adapter = arrayAdapter

        val savedFromCurrency = sharedPreferences.getString("fromCurrency", "USD")
        val savedToCurrency = sharedPreferences.getString("toCurrency", "SEK")

        currencyList.setSelection(arrayAdapter.getPosition(savedFromCurrency))
        toCurrencyList.setSelection(arrayAdapter.getPosition(savedToCurrency))

        var fromCurrency: String
        var toCurrency: String

        currencyList.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing for now
            }

            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                val editor = sharedPreferences.edit()
                fromCurrency = currencyList.selectedItem.toString()
                toCurrency = toCurrencyList.selectedItem.toString()
                editor.putString("fromCurrency", fromCurrency)
                editor.putString("toCurrency", toCurrency)
                editor.apply()
                extractConversionRate(sharedPreferences.getString("fromCurrency", "USD")
                    ?: fromCurrency, sharedPreferences.getString("toCurrency", "SEK") ?: toCurrency)
            }
        }
        toCurrencyList.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing for now
            }

            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                val editor = sharedPreferences.edit()
                fromCurrency = currencyList.selectedItem.toString()
                toCurrency = toCurrencyList.selectedItem.toString()
                editor.putString("fromCurrency", fromCurrency)
                editor.putString("toCurrency", toCurrency)
                editor.apply()
                extractConversionRate(sharedPreferences.getString("fromCurrency", "USD")
                    ?: fromCurrency, sharedPreferences.getString("toCurrency", "SEK") ?: toCurrency)
            }
        }

        convertButton.setOnClickListener {
            val savedToCurrency = sharedPreferences.getString("toCurrency", "SEK")
            val conversionRate = extractConversionRate(sharedPreferences.getString("fromCurrency", "USD")
                ?: "USD", savedToCurrency ?: "SEK")


            val amountEntered = currencyAmount.text ?: ""
            val convertedAmountinSEK: Double = if (!amountEntered.isEmpty()) {
                amountEntered.toString().toFloat() * conversionRate
            } else 0.0
            val toast = Toast.makeText(this, "$savedToCurrency: " + String.format("%.2f", convertedAmountinSEK), Toast.LENGTH_LONG)
            val toastView = toast.view
            toastView.background.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN)

            textView.text = String.format("%.2f", convertedAmountinSEK)

            val messageView = toastView.findViewById<TextView>(android.R.id.message)
            messageView.setTextColor(Color.WHITE)
            toast.show()
        }
    }
}
