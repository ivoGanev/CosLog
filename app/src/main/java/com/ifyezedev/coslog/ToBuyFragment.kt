package com.ifyezedev.coslog

import android.view.View
import com.ifyezedev.coslog.CosplayBaseFragment
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.databinding.FragmentToBuyBinding

class ToBuyFragment : CosplayBaseFragment<FragmentToBuyBinding>(), View.OnClickListener {
    override fun bindingLayoutId(): Int = R.layout.fragment_to_buy

    override fun onBindingCreated() {
        binding.bottom.buttonAddImage.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonAddImage -> onAddImage()
        }
    }

    private fun onAddImage() {
        cosplayNavController.navigate(R.id.pictureViewerFragment)
    }
}