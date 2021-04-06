package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.ifyezedev.coslog.core.common.BaseFragment


abstract class CosplayBaseFragment<T : ViewDataBinding> : BaseFragment<T>() {
    val compositionRoot by lazy {
        CosplayFragmentCompositionRoot(requireActivity())
    }
    
    abstract override fun bindingLayoutId(): Int
}
