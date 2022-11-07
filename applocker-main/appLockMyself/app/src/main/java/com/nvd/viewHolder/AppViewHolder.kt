package com.nvd.viewHolder

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nvd.applocker.R

class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var icon: ImageView
    var name: TextView
    var size: TextView
    var isFavorite : CheckBox
    var isLocked : CheckBox

    init {
        icon = itemView.findViewById(R.id.app_icon)
        name = itemView.findViewById(R.id.app_name)
        size = itemView.findViewById(R.id.app_size)
        isFavorite = itemView.findViewById(R.id.isFavorite)
        isLocked = itemView.findViewById(R.id.isLock)
    }

}
