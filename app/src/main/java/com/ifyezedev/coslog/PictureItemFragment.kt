package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.databinding.PictureItemBinding
import com.ifyezedev.coslog.feature.elements.internal.LoadBitmapsFromInternalStorageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PictureItemFragment : BaseFragment<PictureItemBinding>() {
    override fun bindingLayoutId(): Int = R.layout.picture_item

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val path = arguments?.getString(PictureGalleryFragment.Keys.IMAGE_PATH)
        val loadFromInternalStorageUseCase = LoadBitmapsFromInternalStorageUseCase()

        lifecycleScope.launch {
            loadFromInternalStorageUseCase.invoke(path!!)
            { bitmap ->

                launch(Dispatchers.Main) {
                    binding.imageView.setImageBitmap(bitmap)
                }
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}