package com.ifyezedev.coslog

import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.core.di.fragment.CosplayFragmentComponent
import com.ifyezedev.coslog.core.di.fragment.DaggerCosplayFragmentComponent
import com.ifyezedev.coslog.core.common.ImageFilePathProvider

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

    protected fun hideOverflowMenu(menu: Menu) {
        val item1 = menu.findItem(R.id.edit_cosplay)
        val item2 = menu.findItem(R.id.mark_completed)
        val item3 = menu.findItem(R.id.view_summary)
        if (item1 != null && item2 != null && item3 != null) {
            item1.isVisible = false
            item2.isVisible = false
            item3.isVisible = false
        }
    }

    @CallSuper
    override fun onAfterBindingCreated(view: View) {
        cosplayController = cosplayFragmentComponent.cosplayController()
        imageFilePathPathProvider = cosplayFragmentComponent.imageFilePathProvider()
        actionBar = cosplayFragmentComponent.actionBar()
        // Mote that when we navigate to a new fragment the
        // action bar will be automatically reset to the app name.
        // If the title needs to be set to another we need to make sure that
        // we don't do it in this method.
        actionBar.setTitle(R.string.app_name)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            cosplayController.navigateUp()
        }
        return true
    }

    abstract override fun bindingLayoutId(): Int
}