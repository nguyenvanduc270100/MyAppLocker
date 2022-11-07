package com.nvd.fragment

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nvd.adapter.AdapterApp
import com.nvd.adapter.AdapterSearchApp
import com.nvd.adapter.ViewPagerAdapter
import com.nvd.applocker.R
import com.nvd.model.App
import com.nvd.utils.Utils
import java.util.*
import kotlin.collections.ArrayList

class AppFragment :Fragment() {

    var adapterApp : AdapterSearchApp? = null
    var mTabLayout : TabLayout? = null
    var mRcvListSearch: RecyclerView? = null
    val listSearch = ArrayList<String>()
    var listAppSearch = ArrayList<App>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_apps, container, false)
        var mSearch : SearchView = view.findViewById(R.id.sreachView)
        mRcvListSearch = view.findViewById(R.id.listSearch)
        mTabLayout = view.findViewById(R.id.tabLayout)
        val mViewPager : ViewPager2 = view.findViewById(R.id.viewPager)

        val viewPagerAdapter = ViewPagerAdapter(requireActivity())

        mViewPager.adapter = viewPagerAdapter

        val until = Utils(requireActivity())
        val listApp = until.getAlldApps()

        TabLayoutMediator(mTabLayout!!,mViewPager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when(position){
                0 -> tab.text = resources.getString(R.string.tab_allApp)
                1 -> tab.text = resources.getString(R.string.tab_installedApp)
                2 -> tab.text = resources.getString(R.string.tab_favoriteApp)
            }
        } ).attach()
        setTabDividers()

        mRcvListSearch!!.layoutManager = LinearLayoutManager(activity)
        adapterApp = AdapterSearchApp(requireContext())

        adapterApp!!.setData(listApp)
        mRcvListSearch!!.adapter = adapterApp

        for (item in listApp){
            listSearch.add(item.name.toString())
        }

        mSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isEmpty()){
                    mRcvListSearch!!.visibility = View.GONE
                    mTabLayout!!.visibility = View.VISIBLE
                    mViewPager.visibility = View.VISIBLE
                } else {
                    mRcvListSearch!!.visibility = View.VISIBLE
                    mTabLayout!!.visibility = View.GONE
                    mViewPager.visibility = View.GONE
                    listAppSearch.clear()
                    for (item in listApp) {
                        if (item.name!!.lowercase(Locale.getDefault()).contains(newText)) {
                            listAppSearch.add(item)
                        }
                    }
                    adapterApp!!.setData(listAppSearch)
                }

                return true
            }
        })
        return view
    }
    private fun setTabDividers() {
        val root: View = mTabLayout!!.getChildAt(0)
        if (root is LinearLayout) {
            root.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(Color.BLUE)
            drawable.setSize(2, 1)
            root.dividerPadding = 10
            root.dividerDrawable = drawable
        }
    }

}