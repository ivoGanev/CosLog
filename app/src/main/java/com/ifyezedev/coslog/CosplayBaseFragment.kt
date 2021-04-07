package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.MaterialToolbar
import com.ifyezedev.coslog.core.common.BaseFragment


abstract class CosplayBaseFragment<T : ViewDataBinding> : BaseFragment<T>() {
    private lateinit var compositionRoot: CosplayFragmentCompositionRoot

    lateinit var cosplayController: NavController
    lateinit var activity: CosplayActivity

    override fun onStart() {
        super.onStart()
        compositionRoot = CosplayFragmentCompositionRoot(requireActivity() as CosplayActivity)
        cosplayController = compositionRoot.cosplayController
    }

    abstract override fun bindingLayoutId(): Int
}
