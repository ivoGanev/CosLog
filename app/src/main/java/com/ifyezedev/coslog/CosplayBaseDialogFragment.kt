package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.ifyezedev.coslog.core.common.BaseDialogFragment

abstract class CosplayBaseDialogFragment<T : ViewDataBinding> : BaseDialogFragment<T>() {
    lateinit var compositionRoot: CosplayFragmentCompositionRoot

    abstract override fun bindingLayoutId(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositionRoot = CosplayFragmentCompositionRoot(requireActivity())
    }
}