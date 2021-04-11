package com.ifyezedev.coslog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ifyezedev.coslog.PictureViewerFragment.Keys.IMAGE_INDEX
import com.ifyezedev.coslog.PictureViewerFragment.Keys.IMAGE_PATH
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.databinding.FragmentPictureViewerBinding
import com.ifyezedev.coslog.feature.elements.internal.LoadBitmapsFromInternalStorageUseCase
import kotlinx.coroutines.launch
import java.io.File

class PictureViewerFragment : CosplayBaseFragment<FragmentPictureViewerBinding>(),
    View.OnClickListener {

    object Keys {
        const val IMAGE_PATH = "com.ifyezedev.coslog.keys.fragments.image_path"
        const val IMAGE_INDEX = "com.ifyezedev.coslog.keys.fragments.image_index"
    }

    override fun bindingLayoutId(): Int = R.layout.fragment_picture_viewer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dir = File(context?.filesDir, "buy-gallery")
        val galleryData = dir.listFiles().mapIndexed { index, file -> Pair( file.path, index) }

        val imagePagerAdapter = ImagePagerAdapter(this@PictureViewerFragment, galleryData)


        binding {
            imagePager.adapter = imagePagerAdapter
            imagePager.setCurrentItem(requireArguments().getInt(IMAGE_INDEX), false)

            //exitButton.setOnClickListener(this@PictureViewerFragment)
            // shareButton.setOnClickListener(this@PictureViewerFragment)
        }
    }

    class ImagePagerAdapter(
        fragment: Fragment,
        private val data: List<Pair<String, Int>>
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = data.size


        override fun createFragment(position: Int): Fragment {
            val fragment = PictureItemFragment()
            fragment.arguments = Bundle().apply {
                putString(IMAGE_PATH, data[position].first)
                putInt(IMAGE_INDEX, position + 1)
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
        val intent = buildIntent {
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