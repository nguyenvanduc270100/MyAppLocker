package com.nvd.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applock.model.WallpaperInfo
import com.nvd.adapter.WallpaperAdapter
import com.nvd.applocker.R
import com.nvd.interfaces.WallpaperOnClickListener

class ThemeFragment : Fragment() {
    var rcvListTheme : RecyclerView? = null
    var imgPreView : ImageView? = null
    var imgCancel : ImageView? = null
    var imgDone : ImageView? = null
    var adapter : WallpaperAdapter? = null
    var myWall : WallpaperInfo? = null
    lateinit var sharedPreference : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_theme, container, false)
        rcvListTheme = view.findViewById(R.id.list_wall_img_rcv)
        imgPreView = view.findViewById(R.id.preview_background)
        imgCancel = view.findViewById(R.id.cancel)
        imgDone = view.findViewById(R.id.done)

        rcvListTheme!!.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = WallpaperAdapter(requireContext(), getListPaper(), object : WallpaperOnClickListener{
            override fun selectWallpaper(wall : WallpaperInfo) {
                myWall = wall
                imgPreView!!.visibility = View.VISIBLE
                rcvListTheme!!.visibility = View.GONE
                imgCancel!!.visibility = View.VISIBLE
                imgDone!!.visibility = View.VISIBLE
                imgPreView!!.setImageResource(wall.imageRes)

            }
        })
        imgDone!!.setOnClickListener{
            sharedPreference =  requireContext().getSharedPreferences("DataLocal", Context.MODE_PRIVATE)
            editor = sharedPreference.edit()
            editor.putInt("wallpaper", myWall!!.imageRes)
            editor.apply()
            imgPreView!!.visibility = View.GONE
            rcvListTheme!!.visibility = View.VISIBLE
            imgCancel!!.visibility = View.GONE
            imgDone!!.visibility = View.GONE
            Toast.makeText(requireContext(), "successfull", Toast.LENGTH_SHORT).show()
        }
        imgCancel!!.setOnClickListener{
            imgPreView!!.visibility = View.GONE
            rcvListTheme!!.visibility = View.VISIBLE
            imgCancel!!.visibility = View.GONE
            imgDone!!.visibility = View.GONE
        }
        rcvListTheme!!.adapter = adapter

        return view
    }

    fun getListPaper() : List<WallpaperInfo>{
        var list = ArrayList<WallpaperInfo>()
        list.add(WallpaperInfo(R.drawable.backgroundone))
        list.add(WallpaperInfo(R.drawable.backfroundtwo))
        list.add(WallpaperInfo(R.drawable.backgroundthree))
        list.add(WallpaperInfo(R.drawable.backgroundfour))
        list.add(WallpaperInfo(R.drawable.backgroundfive))
        list.add(WallpaperInfo(R.drawable.backgroundsix))
        list.add(WallpaperInfo(R.drawable.backgroundseven))
        list.add(WallpaperInfo(R.drawable.backgroundeight))
        list.add(WallpaperInfo(R.color.white))

        return list
    }
}