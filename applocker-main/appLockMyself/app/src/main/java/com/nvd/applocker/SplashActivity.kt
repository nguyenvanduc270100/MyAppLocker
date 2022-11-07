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
import androidx.appcompat.app.AppCompatActivity
import com.nvd.service.Actions
import com.nvd.service.EndlessService
import com.nvd.service.ServiceState
import com.nvd.service.getServiceState
import com.nvd.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    var isSetPass : Boolean? = null

    lateinit var sharedPreference : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreference =  getSharedPreferences("DataLocal", Context.MODE_PRIVATE)
        isSetPass = sharedPreference.getBoolean("set_pass", true)

        if (!Settings.canDrawOverlays(this) || !checkPermission(this)){
            CoroutineScope(Dispatchers.Main).launch {

                delay(5000)
                startActivity(Intent(this@SplashActivity, LanguageActivity::class.java))
            }
        } else{
            CoroutineScope(Dispatchers.Main).launch {

                delay(5000)
                if (isSetPass == true){
                    startActivity(Intent(this@SplashActivity, SetPasswordActivity::class.java))
                } else{
                    startActivity(Intent(this@SplashActivity, RequestPasswordActivity::class.java))
                }
            }
        }
    }

    fun checkPermission(ctx: Context): Boolean {
        val mode: Int
        val appOpsManager = ctx.getSystemService(AppCompatActivity.APP_OPS_SERVICE) as AppOpsManager
        mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpsManager.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                ctx.packageName
            )
        } else {
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                ctx.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

}