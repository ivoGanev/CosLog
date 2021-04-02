package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.ifyezedev.coslog.databinding.FragmentToBuyBinding

class ToBuyFragment : BindingFragment<FragmentToBuyBinding>(), View.OnClickListener {
    override fun bindingLayoutId(): Int = R.layout.fragment_to_buy

    override fun onBindingCreated() {
        binding.bottom.buttonAddImage.setOnClickListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonAddImage -> onAddImage()
        }
    }

    private fun onAddImage() {
        val navHostFragment = requireActivity()
            .supportFragmentManager
            .findFragmentById(R.id.cosplayNavHostFragment) as NavHostFragment
        navHostFragment.navController.navigate(R.id.pictureViewerFragment)
    }
}