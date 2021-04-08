package com.ifyezedev.coslog.core.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ifyezedev.coslog.core.io.BitmapResolver

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    @LayoutRes
    abstract fun bindingLayoutId(): Int

    private lateinit var bindingAgent: LayoutBindingAgent<T>

    val binding: T get() = bindingAgent.binding

    lateinit var application: BaseApplication
    lateinit var bitmapResolver: BitmapResolver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingAgent = StandardBindingAgent(bindingLayoutId(), inflater, container)
        return  bindingAgent.bind()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        application = requireActivity().application as BaseApplication
        bitmapResolver = application.bitmapResolver
    }

    fun binding(init: T.() -> Unit) {
        binding.init()
    }

    override fun onDestroyView() {
        bindingAgent.destroy()
        super.onDestroyView()
    }
}
