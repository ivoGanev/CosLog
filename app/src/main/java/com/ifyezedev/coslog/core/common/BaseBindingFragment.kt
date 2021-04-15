package com.ifyezedev.coslog.core.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseBindingFragment<T : ViewDataBinding> : BaseFragment() {

    @LayoutRes
    abstract fun bindingLayoutId(): Int

    private lateinit var bindingAgent: LayoutBindingAgent<T>

    val binding: T get() = bindingAgent.binding

    lateinit var application: BaseApplication

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingAgent = StandardBindingAgent(bindingLayoutId(), inflater, container)
        onAfterBindingCreated()
        return bindingAgent.bind()
    }

    open fun onAfterBindingCreated() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        application = requireActivity().application as BaseApplication
    }

    fun binding(init: T.() -> Unit) {
        binding.init()
    }

    override fun onDestroyView() {
        bindingAgent.destroy()
        super.onDestroyView()
    }
}
