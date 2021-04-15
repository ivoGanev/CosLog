package com.ifyezedev.coslog.core.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase
import com.ifyezedev.coslog.core.di.fragment.DaggerFragmentComponent
import com.ifyezedev.coslog.core.di.fragment.FragmentComponent
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding> :Fragment() {
    lateinit var deleteBitmapsFromInternalStorageUseCase: DeleteBitmapsFromInternalStorageUseCase

    lateinit var loadBitmapsFromInternalStorageUseCase: LoadBitmapsFromInternalStorageUseCase

    lateinit var saveBitmapsToInternalStorageUseCase: SaveBitmapsToInternalStorageUseCase

    private val baseFragmentComponent: FragmentComponent by lazy {
        DaggerFragmentComponent.builder()
            .appComponent((requireActivity().application as BaseApplication).appComponent)
            .build()
    }

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
        deleteBitmapsFromInternalStorageUseCase = baseFragmentComponent.deleteBitmapsFromInternalStorageUseCase()
        loadBitmapsFromInternalStorageUseCase = baseFragmentComponent.loadBitmapsFromInternalStorageUseCase()
        saveBitmapsToInternalStorageUseCase = baseFragmentComponent.saveBitmapsToInternalStorageUseCase()

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

    fun toastNotify(message: String) = (requireActivity() as BaseActivity<*>).toastNotify(message)
}
