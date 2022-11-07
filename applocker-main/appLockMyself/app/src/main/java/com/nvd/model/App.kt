package com.nvd.model

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey


class App {


    private var nameApp : String? = null
    private var iconApp : Drawable? = null
    private var sizeApp : String? = null
    private var packageApp : String? = null
    private var isFavorite : Boolean? = null
    private var isLocked : Boolean? = null

    constructor(nameApp : String, iconApp : Drawable?, packageApp : String ){
        this.nameApp = nameApp
        this.iconApp = iconApp
        this.packageApp = packageApp
    }

    var name : String?
    get() {return nameApp}
    set(value) {this.nameApp = value}

    var icon : Drawable?
    get() {return iconApp}
    set(value) {this.iconApp = value}

    var size : String?
        get() {return sizeApp}
        set(value) {this.sizeApp = value}

    var PKName : String?
        get() {return packageApp}
        set(value) {this.packageApp = value}

    var IsFavorite : Boolean?
        get() {return isFavorite}
        set(value) {this.isFavorite = value}

    var IsLocked : Boolean?
        get() {return isLocked}
        set(value) {this.isLocked = value}
}