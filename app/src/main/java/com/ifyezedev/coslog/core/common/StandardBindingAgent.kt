package com.ifyezedev.coslog.core.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.lang.NullPointerException
import java.lang.reflect.ParameterizedType

class StandardBindingAgent<T : ViewDataBinding>(
    val bindingLayoutId: Int,
    private val inflater: LayoutInflater,
    private val container: ViewGroup?,
) : LayoutBindingAgent<T> {

    private var _binding: T? = null
    override val binding: T get() = _binding!!

    override fun bind(): View {
        try {
            _binding = DataBindingUtil.inflate(inflater, bindingLayoutId, container, false)
        } catch (ex: NullPointerException) {
            val bindingType = (this.javaClass.genericSuperclass as ParameterizedType)
                .actualTypeArguments[0] as Class<*>
            throw NullPointerException(
                "\n Unable to initialize binding. " +
                        "\n ${this::class} - The binding type $bindingType might not be referencing the correct layout."
            )
        }
        return binding.root
    }

    fun binding(init: T.() -> Unit) {
        binding.init()
    }

    override fun destroy() {
        _binding = null
    }

    override fun bindingLayoutId(): Int = bindingLayoutId
}
