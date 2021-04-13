package com.ifyezedev.coslog

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.common.BaseFragment


abstract class CosplayBaseFragment<T : ViewDataBinding> : BaseFragment<T>() {

    private val compositionRoot: CosplayFragmentCompositionRoot by lazy {
        CosplayFragmentCompositionRoot(this)
    }

    lateinit var cosplayController: NavController
    lateinit var activity: CosplayActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        inject()
    }

    private fun inject() {
        cosplayController = compositionRoot.cosplayController
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> cosplayController.navigate(R.id.homeActivity)
        }

        return true
    }

    abstract override fun bindingLayoutId(): Int
}

class CosplayFragmentCompositionRoot(fragment: Fragment) {
    val application = fragment.requireActivity().application as BaseApplication

    private val activityCompositionRoot = (fragment.requireActivity() as CosplayActivity).cosplayCompositionRoot

    val cosplayController: NavController = activityCompositionRoot.cosplayController
}
