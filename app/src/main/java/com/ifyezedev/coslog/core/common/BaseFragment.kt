package com.ifyezedev.coslog.core.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromAndroidGallery
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.core.di.fragment.DaggerFragmentComponent
import com.ifyezedev.coslog.core.di.fragment.FragmentComponent
import com.ifyezedev.coslog.feature.elements.internal.ImageFileProvider

abstract class BaseFragment<T : ViewDataBinding> :Fragment() {
    lateinit var deleteBitmapsFromInternalStorage: DeleteBitmapsFromInternalStorage

    lateinit var loadBitmapsFromInternalStorage: LoadBitmapsFromInternalStorage

    lateinit var saveBitmapsToInternalStorage: SaveBitmapsToInternalStorage

    lateinit var loadBitmapsFromAndroidGallery: LoadBitmapsFromAndroidGallery

    lateinit var imageFileProvider: ImageFileProvider

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
        application = requireActivity().application as BaseApplication

        baseFragmentComponent.apply {
            deleteBitmapsFromInternalStorage = deleteBitmapsFromInternalStorageUseCase()
            loadBitmapsFromInternalStorage = loadBitmapsFromInternalStorageUseCase()
            saveBitmapsToInternalStorage = saveBitmapsToInternalStorageUseCase()
            loadBitmapsFromAndroidGallery = getBitmapPathsFromAndroidGallery()
            imageFileProvider = imageFilePathProvider()
        }

        bindingAgent = StandardBindingAgent(bindingLayoutId(), inflater, container)
        val view = bindingAgent.bind()
        onAfterBindingCreated(view)
        return view
    }

    open fun onAfterBindingCreated(view: View) {
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
