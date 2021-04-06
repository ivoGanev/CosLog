package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ifyezedev.coslog.PictureViewerFragment.Keys.IMAGE_PATH
import com.ifyezedev.coslog.databinding.FragmentPictureViewerBinding

class PictureViewerFragment : BindingFragment<FragmentPictureViewerBinding>(),
    View.OnClickListener {

    object Keys {
        const val IMAGE_PATH = "com.ifyezedev.coslog.keys.fragments.image_path"
    }

    override fun bindingLayoutId(): Int = R.layout.fragment_picture_viewer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val imagePagerAdapter = ImagePagerAdapter(this)
        binding.imagePager.adapter = imagePagerAdapter
    }

    class ImagePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 100

        override fun createFragment(position: Int): Fragment {
            val fragment = PictureItemFragment()
            fragment.arguments = Bundle().apply {
                putInt(IMAGE_PATH, position + 1)
            }
            return fragment
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.exitButton -> onExitButtonClicked()
            R.id.shareButton -> onShareButtonClicked()
        }
    }

    private fun onShareButtonClicked() {
    }

    private fun onExitButtonClicked() {
    }
}