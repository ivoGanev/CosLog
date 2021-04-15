package com.ifyezedev.coslog

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.core.di.fragment.CosplayFragmentComponent
import com.ifyezedev.coslog.core.di.fragment.DaggerCosplayFragmentComponent


abstract class CosplayFragment<T : ViewDataBinding> : BaseFragment<T>() {

    lateinit var cosplayController: NavController
    lateinit var toolbar: Toolbar

    private val cosplayFragmentComponent: CosplayFragmentComponent by lazy {
        DaggerCosplayFragmentComponent.builder()
            .cosplayActivityComponent((requireActivity() as CosplayActivity).cosplayActivityComponent)
            .build()
    }

    override fun onAfterBindingCreated() {
        cosplayController = cosplayFragmentComponent.cosplayController()
        toolbar = cosplayFragmentComponent.toolbar()

        // set the default toolbar title because other classes like, PictureGallery
        // change it dynamically
        toolbar.title = requireContext().resources.getString(R.string.app_name)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> cosplayController.navigate(R.id.homeActivity)
        }

        return true
    }

    abstract override fun bindingLayoutId(): Int
}
