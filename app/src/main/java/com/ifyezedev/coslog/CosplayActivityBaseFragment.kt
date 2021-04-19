package com.ifyezedev.coslog

import android.view.MenuItem
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ifyezedev.coslog.core.common.BaseFragment

abstract class CosplayActivityBaseFragment<T : ViewDataBinding> : BaseFragment<T>() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val controller = (activity as CosplayActivity).cosplayController
        if(item.itemId == android.R.id.home) {
            controller.navigateUp()
        }
        return true
    }

    abstract override fun bindingLayoutId(): Int
}