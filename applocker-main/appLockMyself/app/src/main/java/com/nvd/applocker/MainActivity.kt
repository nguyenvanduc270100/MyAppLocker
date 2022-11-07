package com.nvd.applocker

import android.app.AppOpsManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nvd.AppCompat
import com.nvd.fragment.AppFragment
import com.nvd.fragment.SettingFragment
import com.nvd.fragment.ThemeFragment
import com.nvd.service.Actions
import com.nvd.service.EndlessService
import com.nvd.service.ServiceState
import com.nvd.service.getServiceState
import com.nvd.utils.Utils
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigation : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionOnService(Actions.START)
        bottomNavigation = findViewById(R.id.bottom_navi)

        replaceFragment(AppFragment())
        bottomNavigation.menu.findItem(R.id.bottom_app).setChecked(true)

        bottomNavigation.setOnItemSelectedListener {
            //replaceFragment(AppFragment())
             when(it.itemId) {
                 R.id.bottom_app -> {
                     replaceFragment(AppFragment())
                 }
                 R.id.bottom_theme -> {
                     replaceFragment(ThemeFragment())
                 }

                 R.id.bottom_setting -> {
                     replaceFragment(SettingFragment())
                 }
             }

            true
            //return@setOnItemSelectedListener true
            }


        }
    fun replaceFragment(fragment : Fragment){
        val manager = this.supportFragmentManager
        val transition = manager.beginTransaction()
        transition.replace(R.id.container, fragment)
        transition.commit()
    }

    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(this, EndlessService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //log("Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
            //log("Starting the service in < 26 Mode")
            startService(it)
        }
    }

    fun Share(view: View) {
        val languageManager = LanguageManager(this)
        val check: String = languageManager.lang
        val intentshare = Intent(Intent.ACTION_SEND)
        val url ="https://www.facebook.com/profile.php?id=100027767888969"
        intentshare.type = "text/plain"
        intentshare.putExtra(Intent.EXTRA_TEXT, url)
        val chooseren = Intent.createChooser(intentshare,"Share using....")
        val chooservn = Intent.createChooser(intentshare,"Chia sẻ với....")
        if (check contentEquals "en") {
            startActivity(chooseren)
        }
        if (check contentEquals "vi") {
            startActivity(chooservn)
        }
    }

    fun Rate(view: View) {
        try {
            val uri = Uri.parse("market://details?id=com.cocna.qrcode")
            val intentrate = Intent(Intent.ACTION_VIEW, uri)
            intentrate.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentrate)
        } catch (e: ActivityNotFoundException) {
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.cocna.qrcode")
            val intentrat = Intent(Intent.ACTION_VIEW, uri)
            intentrat.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentrat)
        }
    }

    fun Policy(view: View) {
        val intentpolicy = Intent (this, Policyactivity::class.java)
        startActivity(intentpolicy)
    }

    fun Language(view: View) {
        val intent = Intent (this, LanguageActivity::class.java)
        startActivity(intent)
    }

}






