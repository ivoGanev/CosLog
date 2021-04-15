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
import com.ifyezedev.coslog.core.di.activity.BaseActivityComponent
import com.ifyezedev.coslog.core.di.activity.DaggerBaseActivityComponent
import com.ifyezedev.coslog.core.di.fragment.DaggerFragmentComponent
import com.ifyezedev.coslog.core.di.fragment.FragmentComponent
import javax.inject.Inject

abstract class BaseFragment : Fragment() {
    @Inject
    lateinit var deleteBitmapsFromInternalStorageUseCase: DeleteBitmapsFromInternalStorageUseCase

    @Inject
    lateinit var loadBitmapsFromInternalStorageUseCase: LoadBitmapsFromInternalStorageUseCase

    @Inject
    lateinit var saveBitmapsToInternalStorageUseCase: SaveBitmapsToInternalStorageUseCase

    private val baseFragmentComponent: FragmentComponent by lazy {
        DaggerFragmentComponent.builder()
            .appComponent((requireActivity().application as BaseApplication).appComponent)
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        baseFragmentComponent.inject(this)
        super.onViewCreated(view, savedInstanceState)
    }

    fun toastNotify(message: String) = (requireActivity() as BaseActivity).toastNotify(message)
}
