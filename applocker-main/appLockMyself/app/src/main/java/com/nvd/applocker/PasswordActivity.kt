package com.nvd.applocker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.karikari.goodpinkeypad.GoodPinKeyPad
import com.karikari.goodpinkeypad.KeyPadListerner

class PasswordActivity : AppCompatActivity() {
    var keyPad : GoodPinKeyPad? = null
    var status : TextView? = null
    var layoutRelative : RelativeLayout? = null
    lateinit var sharedPreference : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    var pass : String? = null
    var isSetPass : Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        layoutRelative = findViewById(R.id.layout_password)

        sharedPreference =  getSharedPreferences("DataLocal", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()
        pass = sharedPreference.getString("pass", " ")
        isSetPass  =sharedPreference.getBoolean("set_pass", true)
        val backgroundWall = sharedPreference.getInt("wallpaper", R.color.white)
        layoutRelative!!.background = resources.getDrawable(backgroundWall)
            keyPad = findViewById(R.id.key)
        status = findViewById(R.id.status)




        status!!.text = resources.getString(R.string.enter_password)

        keyPad!!.setKeyPadListener(object : KeyPadListerner {
            override fun onKeyPadPressed(value: String?) {

                if (value == pass){
                    finish()
                } else{
                    val intent = Intent(this@PasswordActivity, PasswordActivity::class.java)
                    startActivity(intent)
                }

            }

            override fun onKeyBackPressed() {
                //implement your code
            }

            override fun onClear() {}
        })
    }

    override fun onBackPressed() {
        Toast.makeText(this,"Enter password",Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
    override fun onStop() {
        super.onStop()
        //finish()
    }
}