package com.ifyezedev.coslog.core.common

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding


interface LayoutBindingAgent<T : ViewDataBinding>{
    val binding: T
    @LayoutRes
    fun bindingLayoutId(): Int
    fun destroy()
    fun bind() : View
}

