package com.nvd.applocker

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.os.Process
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppintroAcivity: AppCompatActivity() {
    var button_Next: ImageButton? = null
    var button_USAGE_ACCESS: ImageButton? = null
    var button_MANAGE_OVERLAY : ImageButton? = null
    var checkResume: Int = 0
    var checkOverplay: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_welcome)

        button_Next = findViewById(R.id.test)
        button_Next!!.setOnClickListener {
            setContentView(R.layout.activity_tutorial_usageaccess)
                button_USAGE_ACCESS = findViewById(R.id.testusage)
                button_USAGE_ACCESS!!.setOnClickListener {
                    startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                }
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkResume >= 1 && checkOverplay == 0) {
            if (!checkPermission(this)) {
                showTostusage()
            } else {
                setContentView(R.layout.activity_tutorial_overlay)
                button_MANAGE_OVERLAY = findViewById(R.id.testOverplay)
                button_MANAGE_OVERLAY!!.setOnClickListener {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivity(intent)
                    checkOverplay ++
                }
            }
        }
        if (checkOverplay > 0){
            if (checkOverplay == 1) {
                if (!Settings.canDrawOverlays(this)) {
                    showTostudisoverplay()
                    checkOverplay ++
                } else {
                    startActivity(Intent(this,SetPasswordActivity::class.java))
                }
            }
            if (checkOverplay > 1) {
                if (!Settings.canDrawOverlays(this)){
                    showTostudisoverplay()
                } else {
                    startActivity(Intent(this,SetPasswordActivity::class.java))
                }
            }
        }
        checkResume ++
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

    private fun showTostusage() {
        val inflater: LayoutInflater = layoutInflater
        val view: View = inflater.inflate(R.layout.toast_custom_layout, findViewById(R.id.toast_root))
        val toast: Toast = Toast(this)
        toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.show()
    }

    private fun showTostudisoverplay() {
        val inflater: LayoutInflater = layoutInflater
        val view: View = inflater.inflate(R.layout.toast_custom_layout_disoverplay, findViewById(R.id.toast_root_disoverplay))
        val toast: Toast = Toast(this)
        toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.show()
    }
}

