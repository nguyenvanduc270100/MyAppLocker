package com.nvd.applocker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.karikari.goodpinkeypad.GoodPinKeyPad
import com.karikari.goodpinkeypad.KeyPadListerner


class SetPasswordActivity : AppCompatActivity() {
    lateinit var sharedPreference : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    var layoutRelative : RelativeLayout? = null
    var keyPad : GoodPinKeyPad? = null
    var status : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_password)
        sharedPreference =  getSharedPreferences("DataLocal",MODE_PRIVATE)
        editor = sharedPreference.edit()
        layoutRelative = findViewById(R.id.layout_set_password)
        val backgroundWall = sharedPreference.getInt("wallpaper", R.color.white)
        layoutRelative!!.background = resources.getDrawable(backgroundWall)

        keyPad = findViewById(R.id.key)
        status = findViewById(R.id.status)

        status!!.text = resources.getString(R.string.set_password)

        keyPad!!.setKeyPadListener(object : KeyPadListerner {
            override fun onKeyPadPressed(value: String?) {
                editor.putString("pass", value)
                editor.apply()
                startActivity(Intent(this@SetPasswordActivity, RequestPasswordActivity::class.java ))
            }

            override fun onKeyBackPressed() {
                //implement your code
            }

            override fun onClear() {}
        })


    }
}