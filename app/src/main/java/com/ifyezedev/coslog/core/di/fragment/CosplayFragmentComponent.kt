package com.ifyezedev.coslog.core.di.fragment

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import com.ifyezedev.coslog.core.di.activity.CosplayActivityComponent
import dagger.Component

@Component(dependencies = [CosplayActivityComponent::class])
@CosplayFragmentScope
interface CosplayFragmentComponent {
    fun cosplayController() : NavController
}