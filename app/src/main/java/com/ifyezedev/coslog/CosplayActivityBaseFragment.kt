package com.ifyezedev.coslog

import android.view.MenuItem
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.core.di.activity.CosplayActivityComponent
import com.ifyezedev.coslog.core.di.fragment.CosplayFragmentComponent
import com.ifyezedev.coslog.core.di.fragment.DaggerCosplayFragmentComponent
import com.ifyezedev.coslog.feature.elements.internal.FilePathProvider
import com.ifyezedev.coslog.feature.elements.internal.ImageFileProvider

abstract class CosplayActivityBaseFragment<T : ViewDataBinding> : BaseFragment<T>() {

    private val cosplayFragmentComponent: CosplayFragmentComponent by lazy {
        DaggerCosplayFragmentComponent.builder()
            .cosplayActivityComponent((requireActivity() as CosplayActivity).cosplayActivityComponent)
            .appComponent(application.appComponent)
            .build()
    }

    lateinit var cosplayController: NavController

    lateinit var imageFilePathProvider: ImageFileProvider

    override fun onAfterBindingCreated(view: View) {
        cosplayController = cosplayFragmentComponent.cosplayController()
        imageFilePathProvider = cosplayFragmentComponent.imageFilePathProvider()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val controller = (activity as CosplayActivity).cosplayController
        if (item.itemId == android.R.id.home) {
            controller.navigateUp()
        }
        return true
    }

    abstract override fun bindingLayoutId(): Int
}