package com.ifyezedev.coslog

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.common.BaseBindingFragment
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.core.di.activity.CosplayActivityComponent
import com.ifyezedev.coslog.core.di.activity.CosplayActivityModule
import com.ifyezedev.coslog.core.di.activity.DaggerCosplayActivityComponent
import com.ifyezedev.coslog.core.di.fragment.CosplayFragmentComponent
import com.ifyezedev.coslog.core.di.fragment.DaggerCosplayFragmentComponent


abstract class CosplayBaseFragment<T : ViewDataBinding> : BaseBindingFragment<T>() {

    lateinit var cosplayController: NavController

    private val cosplayFragmentComponent: CosplayFragmentComponent by lazy {
        DaggerCosplayFragmentComponent.builder()
            .cosplayActivityComponent((requireActivity() as CosplayActivity).cosplayActivityComponent)
            .build()
    }

    override fun onAfterBindingCreated() {
        cosplayController = cosplayFragmentComponent.cosplayController()
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
