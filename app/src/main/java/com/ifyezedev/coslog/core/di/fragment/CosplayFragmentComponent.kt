package com.ifyezedev.coslog.core.di.fragment

import androidx.appcompat.app.ActionBar
import androidx.navigation.NavController
import com.ifyezedev.coslog.core.di.activity.CosplayActivityComponent
import com.ifyezedev.coslog.core.di.app.AppComponent
import com.ifyezedev.coslog.feature.elements.internal.ImageFilePathProvider
import dagger.Component

@Component(dependencies = [CosplayActivityComponent::class, AppComponent:: class])
@CosplayFragmentScope
interface CosplayFragmentComponent {
    fun cosplayController() : NavController
    fun imageFilePathProvider() : ImageFilePathProvider
    fun actionBar() : ActionBar
}