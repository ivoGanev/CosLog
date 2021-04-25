package com.ifyezedev.coslog.core.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ifyezedev.coslog.core.common.usecase.DeleteFilesFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromAndroidGallery
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.core.di.fragment.DaggerFragmentComponent
import com.ifyezedev.coslog.core.di.fragment.FragmentComponent
import com.ifyezedev.coslog.feature.elements.internal.FilePathProvider

/**
 * Inherit from the base fragment when you need access to global services like
 * deleting, saving and loading bitmaps.
 *
 * Another use of the base fragment is to automatically assign a binding between
 * the layout and the class object and automatically clean it.
 *
 * How to use:
 * If you have an activity just inherit from it like:
 *
 * ```
 * class MyFragment : BaseFragment<MyFragmentBinding> {
 * ..
 *   override fun bindingLayoutId(): Int = R.layout.myFragment_layout
 * }
 * ```
 *
 * */
abstract class BaseFragment<T : ViewDataBinding> :Fragment() {
    lateinit var deleteFilesFromInternalStorage: DeleteFilesFromInternalStorage

    lateinit var loadBitmapsFromInternalStorage: LoadBitmapsFromInternalStorage

    lateinit var saveBitmapsToInternalStorage: SaveBitmapsToInternalStorage

    lateinit var loadBitmapsFromAndroidGallery: LoadBitmapsFromAndroidGallery

    lateinit var filePathProvider: FilePathProvider

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

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        application = requireActivity().application as BaseApplication

        baseFragmentComponent.apply {
            deleteFilesFromInternalStorage = deleteBitmapsFromInternalStorageUseCase()
            loadBitmapsFromInternalStorage = loadBitmapsFromInternalStorageUseCase()
            saveBitmapsToInternalStorage = saveBitmapsToInternalStorageUseCase()
            loadBitmapsFromAndroidGallery = getBitmapPathsFromAndroidGallery()
            filePathProvider = imageFilePathProvider()
        }

        bindingAgent = StandardBindingAgent(bindingLayoutId(), inflater, container)
        val view = bindingAgent.bind()
        onAfterBindingCreated(view)
        return view
    }

    /**
     * Override this function if you need access to [onCreateView] after the binding has
     * been created.
     * */
    open fun onAfterBindingCreated(view: View) {
    }

    fun binding(init: T.() -> Unit) {
        binding.init()
    }

    @CallSuper
    override fun onDestroyView() {
        bindingAgent.destroy()
        super.onDestroyView()
    }

    fun toastNotify(message: String) = (requireActivity() as BaseActivity<*>).toastNotify(message)
}
