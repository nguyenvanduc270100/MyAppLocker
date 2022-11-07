package com.nvd.adapter

import android.content.Context
import android.content.SharedPreferences
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.nvd.applocker.R

import com.nvd.model.App
import com.nvd.viewHolder.AppViewHolder
import java.io.File


class AdapterSearchApp(private val context: Context) : RecyclerView.Adapter<AppViewHolder>() {

    var listApp = ArrayList<App>()
    lateinit var sharedPreference : SharedPreferences
    lateinit var favoritePreference : SharedPreferences

    fun setData(listApp : ArrayList<App>){
        this.listApp = listApp
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.app_item, parent, false)
        sharedPreference =  context.getSharedPreferences("AppLock", Context.MODE_PRIVATE)
        favoritePreference =  context.getSharedPreferences("AppFavorite", Context.MODE_PRIVATE)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app: App = listApp.get(position)
        holder.icon.setImageDrawable(app.icon)
        holder.name.setText(app.name)
        val pm = context.packageManager
        val applicationInfo = pm.getApplicationInfo(app.PKName.toString(), 0)
        val file = File(applicationInfo.publicSourceDir)
        app.size = Formatter.formatShortFileSize(context,file.length())
        holder.size.setText(app.size)

        app.IsLocked = sharedPreference.getBoolean(app.name, false)
        if(app.IsLocked == true){

            holder.isLocked.background = context.getDrawable(R.drawable.icon_lock)
        } else{
            holder.isLocked.background = context.getDrawable(R.drawable.icon_unlock)
        }

        holder.isLocked.setOnClickListener {
            app.IsLocked = sharedPreference.getBoolean(app.name, false)
            if (app.IsLocked == true){
                holder.isLocked.background = context.getDrawable(R.drawable.icon_unlock)
                sharedPreference.edit().remove(app.PKName).apply()
                sharedPreference.edit().putBoolean(app.name, false).apply()
            } else{
                holder.isLocked.background = context.getDrawable(R.drawable.icon_lock)
                sharedPreference.edit().putString(app.PKName, app.PKName).apply()
                sharedPreference.edit().putBoolean(app.name, true).apply()
            }
        }

        // FAVORITE

        app.IsFavorite = favoritePreference.getBoolean(app.name, false)

        if (app.IsFavorite == false){
            holder.isFavorite.background = context.getDrawable(R.drawable.icon_dislike)
        } else{
            holder.isFavorite.background = context.getDrawable(R.drawable.icon_like)
        }

        holder.isFavorite.setOnClickListener {
            app.IsFavorite = favoritePreference.getBoolean(app.name, false)
            if (app.IsFavorite == false){
                holder.isFavorite.background = context.getDrawable(R.drawable.icon_like)
                favoritePreference.edit().putBoolean(app.name, true).apply()
                favoritePreference.edit().putString(app.PKName, app.PKName).apply()
            } else {
                holder.isFavorite.background = context.getDrawable(R.drawable.icon_dislike)
                favoritePreference.edit().putBoolean(app.name, false).apply()
                favoritePreference.edit().remove(app.PKName).apply()
            }
        }

    }

    override fun getItemCount(): Int {
       return listApp.size
    }
}