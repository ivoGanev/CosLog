package com.ifyezedev.coslog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ifyezedev.coslog.PictureViewerFragment.Keys.IMAGE_PATH
import com.ifyezedev.coslog.core.builders.intent
import com.ifyezedev.coslog.databinding.FragmentPictureViewerBinding

class PictureViewerFragment : CosplayBaseFragment<FragmentPictureViewerBinding>(),
    View.OnClickListener {

    object Keys {
        const val IMAGE_PATH = "com.ifyezedev.coslog.keys.fragments.image_path"
    }

    override fun bindingLayoutId(): Int = R.layout.fragment_picture_viewer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imagePagerAdapter = ImagePagerAdapter(this)
        binding {
            imagePager.adapter = imagePagerAdapter
            //exitButton.setOnClickListener(this@PictureViewerFragment)
           // shareButton.setOnClickListener(this@PictureViewerFragment)
        }
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
        val intent = intent {
            action = Intent.ACTION_SEND
            type = "image/jpeg"
            putExtra(Intent.EXTRA_TEXT, "My Image")
        }
        startActivity(intent)
    }

    private fun onExitButtonClicked() {
        cosplayController.popBackStack()
    }
}