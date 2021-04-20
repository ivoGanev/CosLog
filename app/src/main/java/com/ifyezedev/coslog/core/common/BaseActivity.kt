package com.ifyezedev.coslog.core.common

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.ifyezedev.coslog.core.common.usecase.DeleteFileFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.core.di.activity.BaseActivityComponent
import com.ifyezedev.coslog.core.di.activity.DaggerBaseActivityComponent

/**
 * Inherit from the base activity when you need access to global services like
 * deleting, saving and loading bitmaps.
 *
 * Another use of the base activity is to automatically assign a binding between
 * the layout and the class object and automatically clean it.
 *
 * How to use:
 * If you have an activity just inherit from it like:
 *
 * ```
 * class MyActivity : BaseActivity<MyActivityBinding> {
 * ..
 *   override fun bindingLayoutId(): Int = R.layout.myActivity_layout
 * }
 * ```
 *
 * */
abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    lateinit var deleteFileFromInternalStorage: DeleteFileFromInternalStorage

    lateinit var loadBitmapsFromInternalStorage: LoadBitmapsFromInternalStorage

    lateinit var saveBitmapsToInternalStorage: SaveBitmapsToInternalStorage

    private val baseActivityComponent: BaseActivityComponent by lazy {
        DaggerBaseActivityComponent.builder()
            .appComponent((application as BaseApplication).appComponent)
            .build()
    }

    @LayoutRes
    abstract fun bindingLayoutId(): Int

    private lateinit var bindingAgent: LayoutBindingAgent<T>

    val binding: T get() = bindingAgent.binding

    fun binding(init: T.() -> Unit) {
        binding.init()
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // sadly we cannot inject those classes because BaseActivity uses generic parameters
        baseActivityComponent.apply {
            deleteFileFromInternalStorage = deleteBitmapsFromInternalStorage()
            loadBitmapsFromInternalStorage = loadBitmapsFromInternalStorage()
            saveBitmapsToInternalStorage = saveBitmapsToInternalStorage()
        }

        bindingAgent = StandardBindingAgent(bindingLayoutId(), layoutInflater, null)
        setContentView(bindingAgent.bind())
    }

    fun toastNotify(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        .show()
}