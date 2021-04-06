package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import com.ifyezedev.coslog.databinding.PictureItemBinding

class PictureItemFragment : CosplayBaseFragment<PictureItemBinding>() {
    override fun bindingLayoutId(): Int = R.layout.picture_item

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.imageView.setImageResource(R.drawable.ic_money)
        super.onViewCreated(view, savedInstanceState)
    }
}