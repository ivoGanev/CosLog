package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.databinding.PictureItemBinding
import com.ifyezedev.coslog.core.functional.onFailure
import com.ifyezedev.coslog.core.functional.onSuccess

class PictureItemFragment : BaseFragment<PictureItemBinding>() {
    override fun bindingLayoutId(): Int = R.layout.picture_item

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val path = arguments?.getString(PictureGalleryFragment.Keys.IMAGE_PATH)!!
        println(path)

        loadBitmapsFromInternalStorage(lifecycleScope, listOf(path))
        { result ->
            result.onSuccess { bitmap -> binding.imageView.setImageBitmap(bitmap[0]) }
            result.onFailure { println(it) }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}