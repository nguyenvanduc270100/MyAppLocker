package com.nvd.viewHolder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.nvd.applocker.R

class WallpaperViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    var iconWall: ImageView
    init {
        iconWall = itemView.findViewById(R.id.wall_paper)
    }
}