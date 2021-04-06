package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.ifyezedev.coslog.core.common.BaseDialogFragment

abstract class CosplayBaseDialogFragment<T : ViewDataBinding> : BaseDialogFragment<T>() {
    val compositionRoot by lazy {
        CosplayFragmentCompositionRoot(requireActivity())
    }
    abstract override fun bindingLayoutId(): Int
}