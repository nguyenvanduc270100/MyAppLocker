package com.nvd.utils

import android.app.AppOpsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import androidx.appcompat.app.AppCompatActivity
import com.nvd.model.App
import io.paperdb.Paper

class Utils(ctx: Context) {

    var packageManager: PackageManager? = ctx.packageManager

    fun checkForLaunchIntent(list: List<ApplicationInfo>): List<ApplicationInfo> {
        val applist = ArrayList<ApplicationInfo>()
        for (info in list) {
            try {
                if (null != packageManager!!.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return applist
    }

    fun getAlldApps(): ArrayList<App> {
        val apps: ArrayList<App> = ArrayList()
        val appList =
            checkForLaunchIntent(packageManager!!.getInstalledApplications(PackageManager.GET_META_DATA))
        for (app in appList) {
            val appName = app.loadLabel(packageManager!!).toString()
            val icon = app.loadIcon(packageManager)
            val pkName = app.packageName
            apps.add(App(appName, icon, pkName))

        }
        return apps
    }
}