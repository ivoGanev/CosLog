package com.ifyezedev.coslog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ifyezedev.coslog.PictureGalleryFragment.Keys.GALLERY_TAG
import com.ifyezedev.coslog.PictureGalleryFragment.Keys.IMAGE_INDEX
import com.ifyezedev.coslog.PictureGalleryFragment.Keys.IMAGE_PATH
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.databinding.FragmentPictureGalleryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    // TODO: Move this to a ViewModel with LiveData eventually
    private var pagePosition: Int = -1

    private lateinit var imagePagerAdapter: ImagePagerAdapter

    private lateinit var toolbar: PictureGalleryToolbar

    override fun onStart() {
        super.onStart()
        val dir = File(context?.filesDir, arguments?.getString(GALLERY_TAG))
        val galleryData = dir.listFiles().mapIndexed { index, file -> Pair(file.path, index) }

        toolbar = cosplayToolbarController.getToolbar(getToolbarType()) as PictureGalleryToolbar

        imagePagerAdapter = ImagePagerAdapter(this@PictureGalleryFragment,
            galleryData as MutableList<Pair<String, Int>>
        )

        val imageIndex = requireArguments().getInt(IMAGE_INDEX)
        pagePosition = imageIndex + 1

        binding {
            imagePager.adapter = imagePagerAdapter
            imagePager.setCurrentItem(imageIndex, false)

            imagePager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    pagePosition = position + 1
                    toolbar.setTitle("Image: $pagePosition out of ${galleryData.size}")
                }
            })

            deleteImageButton.setOnClickListener(this@PictureGalleryFragment)
        }

        toolbar.setTitle("Image: $pagePosition out of ${galleryData.size}")
        toolbar.setShareButtonListener {
            onShareButtonClicked()
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.deleteImageButton -> deleteImage()
        }
    }

    private fun deleteImage() {

        lifecycleScope.launch {
            val deletePath = imagePagerAdapter.data[pagePosition-1].first
            application.deleteBitmapsFromInternalStorageUseCase.invoke(deletePath)

            launch(Dispatchers.Main){
                imagePagerAdapter.data.removeAt(pagePosition-1)
                imagePagerAdapter.notifyDataSetChanged()
                toolbar.setTitle("Image: $pagePosition out of ${imagePagerAdapter.data.size}")

                if(imagePagerAdapter.data.size ==0)
                    cosplayController.popBackStack()
            }
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


    class ImagePagerAdapter(
        fragment: Fragment,
        val data: MutableList<Pair<String, Int>>
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
}