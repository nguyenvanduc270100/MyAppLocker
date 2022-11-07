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

class RequestPasswordActivity : AppCompatActivity() {
    var keyPad : GoodPinKeyPad? = null
    var status : TextView? = null
    lateinit var sharedPreference : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    var pass : String? = null
    var isSetPass : Boolean? = null
    var layoutRelative : RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_password)

        sharedPreference =  getSharedPreferences("DataLocal", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()
        layoutRelative = findViewById(R.id.change)
        pass = sharedPreference.getString("pass", " ")
        isSetPass  =sharedPreference.getBoolean("set_pass", true)
        val backgroundWall = sharedPreference.getInt("wallpaper", R.color.white)
        layoutRelative!!.background = resources.getDrawable(backgroundWall)
        keyPad = findViewById(R.id.key)
        status = findViewById(R.id.status)



        if (isSetPass == true){
            status!!.text = resources.getString(R.string.confirm_password)
            isSetPass = false
            editor.putBoolean("set_pass", isSetPass!!).apply()

            keyPad!!.setKeyPadListener(object : KeyPadListerner {
                override fun onKeyPadPressed(value: String?) {

                    if (value == pass){
                        val intent = Intent(this@RequestPasswordActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else{

                        isSetPass = true
                        editor.putBoolean("set_pass", isSetPass!!).apply()
                        val intent = Intent(this@RequestPasswordActivity, RequestPasswordActivity::class.java)
                        startActivity(intent)
                    }

                }

                override fun onKeyBackPressed() {
                    //implement your code
                }

                override fun onClear() {}
            })

        } else{
            status!!.text = resources.getString(R.string.enter_password)

            keyPad!!.setKeyPadListener(object : KeyPadListerner {
                override fun onKeyPadPressed(value: String?) {

                    if (value == pass){
                        val intent = Intent(this@RequestPasswordActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else{
                        val intent = Intent(this@RequestPasswordActivity, RequestPasswordActivity::class.java)
                        startActivity(intent)
                    }

                }

                override fun onKeyBackPressed() {
                    //implement your code
                }

                override fun onClear() {}
            })
        }



    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}