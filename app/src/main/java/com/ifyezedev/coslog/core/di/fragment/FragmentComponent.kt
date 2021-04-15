package com.ifyezedev.coslog.core.di.fragment

import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase
import com.ifyezedev.coslog.core.di.activity.BaseActivityComponent
import com.ifyezedev.coslog.core.di.app.AppComponent
import dagger.Component

@Component(dependencies = [AppComponent::class])
@FragmentScope
interface FragmentComponent {
    fun inject(fragment: BaseFragment)
}