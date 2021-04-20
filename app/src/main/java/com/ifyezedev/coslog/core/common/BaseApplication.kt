package com.ifyezedev.coslog.core.common

import android.app.Application
import com.ifyezedev.coslog.core.di.app.AppComponent
import com.ifyezedev.coslog.core.di.app.AppModule
import com.ifyezedev.coslog.core.di.app.DaggerAppComponent

class BaseApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}