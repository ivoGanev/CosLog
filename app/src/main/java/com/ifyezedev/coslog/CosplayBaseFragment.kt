package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.ifyezedev.coslog.core.common.BaseFragment


abstract class CosplayBaseFragment<T : ViewDataBinding> : BaseFragment<T>() {
    lateinit var compositionRoot: CosplayFragmentCompositionRoot

    abstract override fun bindingLayoutId(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositionRoot = CosplayFragmentCompositionRoot(requireActivity())
    }
}
