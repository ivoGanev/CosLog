package com.ifyezedev.coslog.core.di.activity

import androidx.appcompat.app.ActionBar
import androidx.navigation.NavController
import com.ifyezedev.coslog.CosplayActivity
import com.ifyezedev.coslog.core.di.app.AppComponent
import dagger.Component

@Component(modules = [CosplayActivityModule::class], dependencies = [AppComponent::class])
@CosplayActivityScope
interface CosplayActivityComponent {
    fun inject(activity: CosplayActivity)
    fun cosplayController(): NavController
    fun actionBar() : ActionBar
}