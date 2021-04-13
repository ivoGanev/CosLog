package com.ifyezedev.coslog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ifyezedev.coslog.PictureGalleryFragment.Keys.GALLERY_TAG
import com.ifyezedev.coslog.PictureGalleryFragment.Keys.IMAGE_INDEX
import com.ifyezedev.coslog.PictureGalleryFragment.Keys.IMAGE_PATH
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.databinding.FragmentPictureGalleryBinding
import java.io.File

class PictureGalleryFragment : CosplayBaseFragment<FragmentPictureGalleryBinding>(),
    View.OnClickListener {

    object Keys {
        const val IMAGE_PATH = "com.ifyezedev.coslog.keys.fragments.image_path"
        const val IMAGE_INDEX = "com.ifyezedev.coslog.keys.fragments.image_index"
        const val GALLERY_TAG = "com.ifyezedev.coslog.keys.fragments.gallery_tag"
    }

    override fun bindingLayoutId(): Int = R.layout.fragment_picture_gallery



    override fun getToolbarType(): CosplayToolbarController.ToolbarType {
        return CosplayToolbarController.ToolbarType.PictureGallery
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val dir = File(context?.filesDir, arguments?.getString(GALLERY_TAG))
        val galleryData = dir.listFiles().mapIndexed { index, file -> Pair( file.path, index) }
        val toolbar = cosplayToolbarController.getToolbar(getToolbarType()) as PictureGalleryToolbar

        val imagePagerAdapter = ImagePagerAdapter(this@PictureGalleryFragment, galleryData)

        val imageIndex = requireArguments().getInt(IMAGE_INDEX)
        binding {
            imagePager.adapter = imagePagerAdapter
            imagePager.setCurrentItem(imageIndex, false)

            imagePager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
               toolbar.setTitle("Image: ${position+1} out of ${galleryData.size}")
                }
            })
        }

        toolbar.setTitle("Image: ${imageIndex+1} out of ${galleryData.size}")

        toolbar.setShareButtonListener {
            onShareButtonClicked()
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
          //  R.id.exitButton -> onExitButtonClicked()
           // R.id.shareButton -> onShareButtonClicked()
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