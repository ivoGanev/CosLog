package com.ifyezedev.coslog.core.common

import android.app.Application
import com.ifyezedev.coslog.core.io.BitmapResolver

class BaseApplication: Application() {
    lateinit var bitmapResolver: BitmapResolver

    override fun onCreate() {
        bitmapResolver = BitmapResolver(this)
        super.onCreate()
    }
}