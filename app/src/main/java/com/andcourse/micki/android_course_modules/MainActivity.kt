package com.andcourse.micki.android_course_modules

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var convertButton: Button
    private lateinit var imageView: ImageView
    private lateinit var currencyAmount: EditText


    private var currentConversionRate: Double = 0.0

    private fun extractConversionRate(fromCurrency: String, toCurrency: String): Double {
        val apiURL = URL("http://free.currencyconverterapi.com/api/v5/convert?q=$fromCurrency" + "_" + "$toCurrency&compact=y")

        Volley.newRequestQueue(this).add<String?>(StringRequest(Request.Method.GET, apiURL.toString(), Response.Listener<String> { response ->
            val conversionValue = JSONObject(response)
            Log.i("Info", conversionValue.toString())
            currentConversionRate = (conversionValue.get(conversionValue.names().get(0).toString()) as JSONObject)["val"] as Double
        }, Response.ErrorListener { }))
        return currentConversionRate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        convertButton = findViewById(R.id.button)
        imageView = findViewById(R.id.imageView)
        currencyAmount = findViewById(R.id.editText)

        val fromCurrency = "GBP"
        val toCurrency = "SEK"

        extractConversionRate(fromCurrency, toCurrency)

        convertButton.setOnClickListener {
            val amountEntered = currencyAmount.text ?: ""
            val convertedAmountinSEK:Double = if (!amountEntered.isEmpty()) {
                amountEntered.toString().toFloat() * extractConversionRate(fromCurrency, toCurrency)
            } else 0.0
            val toast = Toast.makeText(this, "SEK: " + String.format("%.2f", convertedAmountinSEK), Toast.LENGTH_LONG)
            val toastView = toast.view
            toastView.background.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN)
            val messageView = toastView.findViewById<TextView>(android.R.id.message)
            messageView.setTextColor(Color.WHITE)
            toast.show()
        }
    }
}
