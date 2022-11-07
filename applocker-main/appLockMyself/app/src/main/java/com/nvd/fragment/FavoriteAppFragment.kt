package com.nvd.fragment

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nvd.adapter.AdapterApp
import com.nvd.adapter.AdapterFavoriteApp
import com.nvd.applocker.R
import com.nvd.model.App

class FavoriteAppFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var mLayoutNoApp: LinearLayout
    var apps: ArrayList<App> = ArrayList()
    var list: ArrayList<App> = ArrayList()
    var adapterApp: AdapterFavoriteApp? = null
    var packageManager: PackageManager? = null
    lateinit var favoritePreference: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_app, container, false)
        if (activity != null) {
            packageManager = requireActivity().packageManager
        }


        recyclerView = view.findViewById(R.id.listFavorite)
        mLayoutNoApp = view.findViewById(R.id.layoutNoApp)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapterApp = AdapterFavoriteApp(requireContext())

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
            //val check = favoritePreference.getBoolean(app.name, false)
            if (app.packageName != "com.nvd.applocker") {

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

        favoritePreference =
            requireContext().getSharedPreferences("AppFavorite", Context.MODE_PRIVATE)
        apps = getAlldApps()

        for (item in apps) {
            val check = favoritePreference.getBoolean(item.name, false)
            if (check == true && !list.contains(item)) {
                list.add(item)
            }
        }
        if (list.isEmpty()){
            mLayoutNoApp.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else{
            mLayoutNoApp.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            //adapterApp!!.notifyDataSetChanged()
            adapterApp!!.setData(list)
            recyclerView.adapter = adapterApp

        }

    }
}