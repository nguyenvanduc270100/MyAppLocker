package com.nvd.applocker

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class Policyactivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.private_policy)
        val btn: RelativeLayout = findViewById(R.id.back_privacy)
        btn.setOnClickListener {
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}