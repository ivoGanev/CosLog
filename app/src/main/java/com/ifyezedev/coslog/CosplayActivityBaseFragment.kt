package com.ifyezedev.coslog

import android.app.ActionBar
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.CallSuper
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

/**
 * This fragment should be used by all of the fragments that are residing in the [CosplayActivity].
 *
 * Any fragment that inherits from this one gains:
 * - access to the cosplay NavController
 * - access to the [CosplayActivity] support action bar
 * - the option to override the back button (This is important when we need to navigate to different
 *      destinations that are not withing the cosplay nav controller, e.g. HomeActivity)
 * */
abstract class CosplayActivityBaseFragment<T : ViewDataBinding> : BaseFragment<T>() {

    private val cosplayFragmentComponent: CosplayFragmentComponent by lazy {
        DaggerCosplayFragmentComponent.builder()
            .cosplayActivityComponent((requireActivity() as CosplayActivity).cosplayActivityComponent)
            .appComponent(application.appComponent)
            .build()
    }

    lateinit var cosplayController: NavController

    lateinit var imageFilePathProvider: ImageFileProvider

    lateinit var actionBar : androidx.appcompat.app.ActionBar

    @CallSuper
    override fun onAfterBindingCreated(view: View) {
        cosplayController = cosplayFragmentComponent.cosplayController()
        imageFilePathProvider = cosplayFragmentComponent.imageFilePathProvider()
        actionBar = cosplayFragmentComponent.actionBar()
        setHasOptionsMenu(true)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        // because the PictureGalleryFragment overrides the menu we need to make
        // sure to reset it to it's default state.
        actionBar.setTitle(R.string.app_name)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            cosplayController.navigateUp()
        }
        return true
    }

    abstract override fun bindingLayoutId(): Int
}