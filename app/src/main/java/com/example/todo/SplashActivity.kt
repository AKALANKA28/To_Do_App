package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val textView = findViewById<TextView>(R.id.splashTextView)
        val handler = Handler(Looper.getMainLooper())

        // Handler to delay visibility of TextView with animation
        handler.postDelayed({
            textView.animate().alpha(1f).setDuration(1000).start()
            textView.visibility = TextView.VISIBLE
        }, 1000)  // 1000 milliseconds = 1 second

        // Handler to start MainActivity and finish SplashActivity
        handler.postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 4000)  // 4000 milliseconds = 4 seconds
    }
}
