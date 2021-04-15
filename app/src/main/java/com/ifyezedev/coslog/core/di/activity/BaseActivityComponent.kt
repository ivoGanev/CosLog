package com.ifyezedev.coslog.core.di.activity

import com.ifyezedev.coslog.core.common.BaseActivity
import com.ifyezedev.coslog.core.di.app.AppComponent
import dagger.Component

@Component(modules = [BaseActivityModule::class], dependencies = [AppComponent::class])
@BaseActivityScope
interface BaseActivityComponent {
    fun inject(activity: BaseActivity)
}