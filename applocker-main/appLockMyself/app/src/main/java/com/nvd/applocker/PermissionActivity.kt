package com.nvd.applocker

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.nvd.utils.Utils


class PermissionActivity : AppCompatActivity() {
    var button_USAGE_ACCESS : Button? = null
    var button_MANAGE_OVERLAY : Button? = null
    var button_Next : Button? = null
    lateinit var sharedPreference : SharedPreferences
    var isSetPass : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        button_USAGE_ACCESS = findViewById(R.id.permission_one)
        button_MANAGE_OVERLAY = findViewById(R.id.permission_two)
        button_Next = findViewById(R.id.next)

        sharedPreference =  getSharedPreferences("DataLocal", Context.MODE_PRIVATE)
        isSetPass = sharedPreference.getBoolean("set_pass", true)

        button_USAGE_ACCESS!!.setOnClickListener {
                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }

        button_MANAGE_OVERLAY!!.setOnClickListener {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            //startActivityForResult(intent, 2323)
            startActivity(intent)
        }

        button_Next!!.setOnClickListener {
            val intent = Intent(this, SetPasswordActivity::class.java)
            startActivity(intent)
        }



    }



//    private fun checkPermission(): Boolean {
//        val mode: Int
//        val appOpsManager = getSystemService(APP_OPS_SERVICE) as AppOpsManager
//        mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            appOpsManager.unsafeCheckOpNoThrow(
//                AppOpsManager.OPSTR_GET_USAGE_STATS,
//                Process.myUid(),
//                packageName
//            )
//        } else {
//            appOpsManager.checkOpNoThrow(
//                AppOpsManager.OPSTR_GET_USAGE_STATS,
//                Process.myUid(),
//                packageName
//            )
//        }
//        return mode == AppOpsManager.MODE_ALLOWED
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 2323){
//
//        }
//    }

}