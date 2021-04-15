package com.ifyezedev.coslog.core.common

import android.app.Application
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase
import com.ifyezedev.coslog.core.di.app.AppComponent
import com.ifyezedev.coslog.core.di.app.AppModule
import com.ifyezedev.coslog.core.di.app.DaggerAppComponent
import javax.inject.Inject

class BaseApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}