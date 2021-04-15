package com.ifyezedev.coslog.core.common

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase
import com.ifyezedev.coslog.core.di.activity.BaseActivityComponent
import com.ifyezedev.coslog.core.di.activity.DaggerBaseActivityComponent
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var deleteBitmapsFromInternalStorageUseCase: DeleteBitmapsFromInternalStorageUseCase

    @Inject
    lateinit var loadBitmapsFromInternalStorageUseCase: LoadBitmapsFromInternalStorageUseCase

    @Inject
    lateinit var saveBitmapsToInternalStorageUseCase: SaveBitmapsToInternalStorageUseCase

    private val baseActivityComponent: BaseActivityComponent by lazy {
        DaggerBaseActivityComponent.builder()
            .appComponent((application as BaseApplication).appComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivityComponent.inject(this)
    }

    fun toastNotify(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        .show()
}