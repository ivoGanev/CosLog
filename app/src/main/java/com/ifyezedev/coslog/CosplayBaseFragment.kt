package com.ifyezedev.coslog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.MaterialToolbar
import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.common.BaseFragment


abstract class CosplayBaseFragment<T : ViewDataBinding> : BaseFragment<T>() {

    private val compositionRoot: CosplayFragmentCompositionRoot by lazy {
        CosplayFragmentCompositionRoot((requireActivity() as CosplayActivity).cosplayCompositionRoot)
    }

    lateinit var cosplayController: NavController
    lateinit var activity: CosplayActivity

    override fun onStart() {
        super.onStart()
        inject()
    }

    private fun inject() {
        cosplayController = compositionRoot.cosplayController
    }

    abstract override fun bindingLayoutId(): Int
}

class CosplayFragmentCompositionRoot(private val activityCompositionRoot: CosplayActivityCompositionRoot) {
    val cosplayController: NavController = activityCompositionRoot.cosplayController
}
