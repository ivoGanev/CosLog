package com.ifyezedev.coslog.core.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase
import com.ifyezedev.coslog.core.di.activity.BaseActivityComponent
import com.ifyezedev.coslog.core.di.activity.BaseActivityModule
import com.ifyezedev.coslog.core.di.activity.DaggerBaseActivityComponent
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    lateinit var deleteBitmapsFromInternalStorageUseCase: DeleteBitmapsFromInternalStorageUseCase

    lateinit var loadBitmapsFromInternalStorageUseCase: LoadBitmapsFromInternalStorageUseCase

    lateinit var saveBitmapsToInternalStorageUseCase: SaveBitmapsToInternalStorageUseCase

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
        deleteBitmapsFromInternalStorageUseCase = baseActivityComponent.deleteBitmapsFromInternalStorageUseCase()
        loadBitmapsFromInternalStorageUseCase = baseActivityComponent.loadBitmapsFromInternalStorageUseCase()
        saveBitmapsToInternalStorageUseCase = baseActivityComponent.saveBitmapsToInternalStorageUseCase()

        bindingAgent = StandardBindingAgent(bindingLayoutId(), layoutInflater, null)
        setContentView(bindingAgent.bind())
    }

    fun toastNotify(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        .show()
}