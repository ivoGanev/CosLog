package com.ifyezedev.coslog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import java.lang.NullPointerException
import java.lang.reflect.ParameterizedType

abstract class BindingFragment<T: ViewDataBinding?> : Fragment() {
    private var _binding: T? = null
    protected val binding : T get() = _binding!!

    abstract fun bindingLayoutId() : Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            _binding = DataBindingUtil.inflate(layoutInflater, bindingLayoutId(), container, false)
        }
        catch (ex: NullPointerException) {
            val bindingType = (this.javaClass.genericSuperclass as ParameterizedType)
                .actualTypeArguments[0] as Class<*>
            val resourceName = resources.getResourceName(bindingLayoutId())
            throw NullPointerException("\n Unable to initialize binding. " +
                    "\n - The binding type  $bindingType might not be referencing the layout $resourceName ")
        }
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
