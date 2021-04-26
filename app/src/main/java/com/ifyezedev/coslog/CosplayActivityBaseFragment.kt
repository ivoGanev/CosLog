package com.ifyezedev.coslog

import android.view.MenuItem
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.core.di.fragment.CosplayFragmentComponent
import com.ifyezedev.coslog.core.di.fragment.DaggerCosplayFragmentComponent
import com.ifyezedev.coslog.feature.elements.internal.ImageFilePathProvider

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

    lateinit var imageFilePathPathProvider: ImageFilePathProvider

    lateinit var actionBar: androidx.appcompat.app.ActionBar

    @CallSuper
    override fun onAfterBindingCreated(view: View) {
        cosplayController = cosplayFragmentComponent.cosplayController()
        imageFilePathPathProvider = cosplayFragmentComponent.imageFilePathProvider()
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
        if (item.itemId == android.R.id.home) {
            cosplayController.navigateUp()
        }
        return true
    }

    abstract override fun bindingLayoutId(): Int
}