package com.ifyezedev.coslog

import android.content.Intent
import android.os.Bundle
import android.view.*
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.databinding.FragmentToBuyBinding

class ToBuyFragment : CosplayBaseFragment<FragmentToBuyBinding>(), View.OnClickListener {
    override fun bindingLayoutId(): Int = R.layout.fragment_to_buy

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottom.buttonAddImage.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonAddImage -> onAddImage()
        }
    }

    private fun onAddImage() {
        val intent = buildIntent(Intent.ACTION_PICK) {
            type = "image/*"
        }
        startActivityForResult(intent, 1)
    }
}