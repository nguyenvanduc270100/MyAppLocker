package com.nvd.fragment

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nvd.adapter.AdapterApp
import com.nvd.applocker.R
import com.nvd.model.App
import com.nvd.utils.Utils
import java.io.File




class AllAppFragment : Fragment() {

    lateinit var recyclerView : RecyclerView
    var packageManager: PackageManager? = null
    var apps:  ArrayList<App> = ArrayList()
    var adapterApp : AdapterApp? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_all_app, container, false)
        if (activity != null) {
            packageManager = requireActivity().packageManager
        }
        apps = getAlldApps()

        recyclerView = view.findViewById<RecyclerView>(R.id.rcvAllApp)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapterApp = AdapterApp(requireContext())

//        adapterApp!!.setData(apps)
//        recyclerView.adapter = adapterApp
        return view
    }

    private fun checkForLaunchIntent(list: List<ApplicationInfo>): List<ApplicationInfo> {
        val applist = ArrayList<ApplicationInfo>()
        for (info in list) {
            try {
                if (null != packageManager!!.getLaunchIntentForPackage(info.packageName)) {
                    info
                    applist.add(info)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return applist
    }


    private fun getAlldApps(): ArrayList<App> {
        val apps: ArrayList<App> = ArrayList()
        val appList =
            checkForLaunchIntent(packageManager!!.getInstalledApplications(PackageManager.GET_META_DATA))
        for (app in appList) {

            if(app.packageName != "com.nvd.applocker"){
                val appName = app.loadLabel(requireActivity().packageManager).toString()
                val icon = app.loadIcon(requireActivity().packageManager)
                val pkName = app.packageName
                apps.add(App(appName, icon, pkName))
            }


        }
        return apps
    }

    override fun onResume() {
        super.onResume()
        //adapterApp!!.notifyDataSetChanged()
        adapterApp!!.setData(apps)
        recyclerView.adapter = adapterApp
    }
}