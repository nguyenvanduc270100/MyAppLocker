package com.nvd.fragment

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nvd.adapter.AdapterApp
import com.nvd.adapter.AdapterInstalledApp
import com.nvd.applocker.R
import com.nvd.model.App
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class InstalledAppFragment : Fragment() {
    lateinit var recyclerView : RecyclerView
    var packageManager: PackageManager? = null
    var apps: ArrayList<App> = ArrayList()
    var adapterApp : AdapterInstalledApp? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_installed_app, container, false)
        if (activity != null) {
            packageManager = requireActivity().packageManager
        }

        recyclerView = view!!.findViewById<RecyclerView>(R.id.rcvInstalledApp)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        apps = getInstalledApps()
        adapterApp = AdapterInstalledApp(requireContext())

//        adapterApp!!.setData(apps)
//        recyclerView.adapter = adapterApp
        return view
    }

    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    private fun getInstalledApps(): ArrayList<App> {

        val apps: ArrayList<App> = ArrayList<App>()
        val packs: List<PackageInfo> = packageManager!!.getInstalledPackages(0)

        //List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (i in 0 until (packs.size ?: 0)) {
            val p = packs[i]
            if (!isSystemPackage(p)) {
                if (p.packageName != "com.nvd.applocker"){
                    val appName = p.applicationInfo.loadLabel(requireActivity().packageManager).toString()
                    val icon = p.applicationInfo.loadIcon(requireActivity().packageManager)
                    val pkName = p.packageName
                    apps.add(App(appName, icon, pkName))
                }

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