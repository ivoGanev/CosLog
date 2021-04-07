package com.ifyezedev.coslog.core.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.ifyezedev.coslog.R

abstract class BaseActivity<T: ViewDataBinding> : AppCompatActivity() {

    private lateinit var application: BaseApplication

    @LayoutRes
    abstract fun bindingLayoutId(): Int

    private lateinit var bindingAgent: LayoutBindingAgent<T>

    val binding: T get() = bindingAgent.binding

    fun binding(init: T.() -> Unit) {
        binding.init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        application = getApplication() as BaseApplication
        bindingAgent = StandardBindingAgent(bindingLayoutId(), layoutInflater, null)
        setContentView(bindingAgent.bind())
    }
}