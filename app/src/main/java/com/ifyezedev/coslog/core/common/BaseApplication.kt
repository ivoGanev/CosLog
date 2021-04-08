package com.ifyezedev.coslog.core.common

import android.app.Application
import com.ifyezedev.coslog.core.io.AppBitmapHandler

class BaseApplication: Application() {
    lateinit var appBitmapHandler: AppBitmapHandler

    override fun onCreate() {
        appBitmapHandler = AppBitmapHandler(this)
        super.onCreate()
    }
}