package com.sbz.noteify

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.sbz.noteify.util.LOADING_TIME_IN_MILISEC

class Splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        Handler(Looper.myLooper()!!).postDelayed(
            {
                startActivity(Intent(this@Splashscreen, MainActivity::class.java))
                finish()
            },
            LOADING_TIME_IN_MILISEC
        )

    }
}