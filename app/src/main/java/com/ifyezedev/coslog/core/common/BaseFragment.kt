package com.ifyezedev.coslog.core.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {
    abstract fun bindingLayoutId(): Int

    private lateinit var bindingAgent: LayoutBindingAgent<T>

    val binding: T get() = bindingAgent.binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingAgent = BindingAgentFragment(bindingLayoutId(), inflater, container)
        return  bindingAgent.bind()
    }

    fun binding(init: T.() -> Unit) {
        binding.init()
    }

    override fun onDestroyView() {
        bindingAgent.destroy()
        super.onDestroyView()
    }
}
