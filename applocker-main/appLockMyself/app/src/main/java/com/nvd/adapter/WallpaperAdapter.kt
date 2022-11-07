package com.nvd.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.applock.model.WallpaperInfo
import com.nvd.applocker.R
import com.nvd.interfaces.WallpaperOnClickListener
import com.nvd.viewHolder.AppViewHolder
import com.nvd.viewHolder.WallpaperViewHolder

class WallpaperAdapter(ctx:Context, var listImgPaper : List<WallpaperInfo>, var listener : WallpaperOnClickListener) : RecyclerView.Adapter<WallpaperViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wallpaper, parent, false)
        return WallpaperViewHolder(view)
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val wallPaper = listImgPaper.get(position)
        holder.iconWall.setImageResource(wallPaper.imageRes)
        holder.iconWall.setOnClickListener{
            listener.selectWallpaper(wallPaper)
        }
    }

    override fun getItemCount(): Int {
        if(listImgPaper != null) return listImgPaper.size
        return 0
    }
}