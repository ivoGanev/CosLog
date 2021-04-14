package com.ifyezedev.coslog.core.di.module

import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase
import dagger.Component

@Component(modules = [AppModule::class])
@AppScope
interface AppComponent {
    fun inject(application: BaseApplication)
}