package com.ifyezedev.coslog.core.common

import android.view.View
import androidx.databinding.ViewDataBinding


interface LayoutBindingAgent<T : ViewDataBinding>{
    val binding: T
    fun bindingLayoutId(): Int
    fun destroy()
    fun bind() : View
}

