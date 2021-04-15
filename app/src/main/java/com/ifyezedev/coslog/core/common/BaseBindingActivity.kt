package com.ifyezedev.coslog.core.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.di.activity.BaseActivityComponent
import com.ifyezedev.coslog.core.di.activity.BaseActivityModule
import com.ifyezedev.coslog.core.di.activity.DaggerBaseActivityComponent
import javax.inject.Inject

abstract class BaseBindingActivity<T : ViewDataBinding> : BaseActivity() {
    @LayoutRes
    abstract fun bindingLayoutId(): Int

    private lateinit var bindingAgent: LayoutBindingAgent<T>

    val binding: T get() = bindingAgent.binding

    fun binding(init: T.() -> Unit) {
        binding.init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingAgent = StandardBindingAgent(bindingLayoutId(), layoutInflater, null)
        setContentView(bindingAgent.bind())
    }
}