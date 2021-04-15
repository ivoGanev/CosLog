package com.ifyezedev.coslog.core.di.fragment

import androidx.navigation.NavController
import com.ifyezedev.coslog.CosplayBaseFragment
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase
import com.ifyezedev.coslog.core.di.activity.BaseActivityComponent
import com.ifyezedev.coslog.core.di.activity.CosplayActivityComponent
import com.ifyezedev.coslog.core.di.app.AppComponent
import dagger.Component

@Component(dependencies = [CosplayActivityComponent::class])
@CosplayFragmentScope
interface CosplayFragmentComponent {
    fun cosplayController() : NavController
}