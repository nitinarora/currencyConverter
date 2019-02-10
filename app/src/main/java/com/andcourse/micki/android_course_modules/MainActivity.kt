package com.andcourse.micki.android_course_modules

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var convertButton: Button
    private lateinit var imageView: ImageView
    private lateinit var currencyAmount: EditText

    companion object {
        const val CONVERSION_RATE_USD_TO_SEK:Float = 9.27f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        convertButton = findViewById(R.id.button)
        imageView = findViewById(R.id.imageView)
        currencyAmount = findViewById(R.id.editText)


        convertButton.setOnClickListener {
            val amountEntered = currencyAmount.text ?: ""
            val convertedAmountinSEK = if(!amountEntered.isEmpty()) {
                amountEntered.toString().toFloat() * CONVERSION_RATE_USD_TO_SEK
            } else 0f
            Toast.makeText(this, "SEK: $convertedAmountinSEK", Toast.LENGTH_LONG).show()
        }
    }
}
