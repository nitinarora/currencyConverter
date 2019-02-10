package com.andcourse.micki.android_course_modules

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*

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
            val toast = Toast.makeText(this, "SEK: " + String.format("%.2f", convertedAmountinSEK), Toast.LENGTH_LONG)
            val toastView = toast.view
            toastView.background.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN)
            val messageView = toastView.findViewById<TextView>(android.R.id.message)
            messageView.setTextColor(Color.WHITE)
            toast.show()
        }
    }
}
