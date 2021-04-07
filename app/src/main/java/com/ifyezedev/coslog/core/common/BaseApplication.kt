package com.ifyezedev.coslog.core.common

import android.app.Application
import com.ifyezedev.coslog.core.io.AppBitmapStore

class BaseApplication: Application() {
    lateinit var appBitmapStore: AppBitmapStore

    override fun onCreate() {
        appBitmapStore = AppBitmapStore(this)
        super.onCreate()
    }
}