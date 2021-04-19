package com.ifyezedev.coslog.core.common

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.core.di.activity.BaseActivityComponent
import com.ifyezedev.coslog.core.di.activity.DaggerBaseActivityComponent

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    lateinit var deleteBitmapsFromInternalStorage: DeleteBitmapsFromInternalStorage

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deleteBitmapsFromInternalStorage = baseActivityComponent.deleteBitmapsFromInternalStorage()
        loadBitmapsFromInternalStorage = baseActivityComponent.loadBitmapsFromInternalStorage()
        saveBitmapsToInternalStorage = baseActivityComponent.saveBitmapsToInternalStorage()

        bindingAgent = StandardBindingAgent(bindingLayoutId(), layoutInflater, null)
        setContentView(bindingAgent.bind())
    }

    fun toastNotify(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        .show()
}