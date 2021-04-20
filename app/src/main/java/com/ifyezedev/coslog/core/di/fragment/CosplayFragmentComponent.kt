package com.ifyezedev.coslog.core.di.fragment

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.di.activity.CosplayActivityComponent
import com.ifyezedev.coslog.core.di.app.AppComponent
import com.ifyezedev.coslog.feature.elements.internal.FilePathProvider
import com.ifyezedev.coslog.feature.elements.internal.ImageFileProvider
import dagger.Component

@Component(dependencies = [CosplayActivityComponent::class, AppComponent:: class])
@CosplayFragmentScope
interface CosplayFragmentComponent {
    fun cosplayController() : NavController
    fun imageFilePathProvider() : ImageFileProvider
}