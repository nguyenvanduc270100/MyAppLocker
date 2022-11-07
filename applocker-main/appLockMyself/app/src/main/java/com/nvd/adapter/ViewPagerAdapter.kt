package com.nvd.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nvd.fragment.AllAppFragment
import com.nvd.fragment.FavoriteAppFragment
import com.nvd.fragment.InstalledAppFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllAppFragment()
            1 -> InstalledAppFragment()
            2 -> FavoriteAppFragment()
            else -> AllAppFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}