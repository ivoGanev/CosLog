package com.ifyezedev.coslog

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ifyezedev.coslog.PictureGalleryFragment.Keys.IMAGE_INDEX
import com.ifyezedev.coslog.PictureGalleryFragment.Keys.IMAGE_PATH
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.core.functional.onSuccess
import com.ifyezedev.coslog.databinding.FragmentPictureGalleryBinding

class PictureGalleryFragment : CosplayActivityBaseFragment<FragmentPictureGalleryBinding>() {

    object Keys {
        const val IMAGE_PATH = "com.ifyezedev.coslog.keys.fragments.image_path"
        const val IMAGE_INDEX = "com.ifyezedev.coslog.keys.fragments.image_index"
    }

    override fun bindingLayoutId(): Int = R.layout.fragment_picture_gallery

    // TODO: Move this to a ViewModel with LiveData eventually
    private var pagePosition: Int = -1

    private lateinit var imagePagerAdapter: ImagePagerAdapter

    override fun onAfterBindingCreated(view: View) {
        super.onAfterBindingCreated(view)
        setHasOptionsMenu(true);
    }

    override fun onStart() {
        super.onStart()
        val internalStorageImagePaths = imageFileProvider.getInternalStorageImageFilePaths()

        // if there are any images to display from internal storage
        if (internalStorageImagePaths != null) {
            imagePagerAdapter = ImagePagerAdapter(
                this@PictureGalleryFragment,
                internalStorageImagePaths.toMutableList()
            )

            val imageIndex = requireArguments().getInt(IMAGE_INDEX)
            pagePosition = imageIndex

            binding {
                imagePager.adapter = imagePagerAdapter
                imagePager.setCurrentItem(imageIndex, false)

                imagePager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        pagePosition = position
                        updateToolbarTitle()
                    }
                })
            }

            updateToolbarTitle()
        }
        // otherwise inform that there are no images
        else {
        }
    }

    private fun deleteImage() {
        val deletePath = imagePagerAdapter.getItem(pagePosition)

        deleteFileFromInternalStorage(lifecycleScope, deletePath) { result ->
            result.onSuccess {
                println(it)
                imagePagerAdapter.removeAt(pagePosition)
                updateToolbarTitle()
            }
        }
    }

    private fun updateToolbarTitle() {
        actionBar.title = "Image: ${pagePosition + 1} out of ${imagePagerAdapter.getDataSize()}"
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1 = menu.findItem(R.id.edit_cosplay)
        val item2 = menu.findItem(R.id.mark_completed)
        val item3 = menu.findItem(R.id.view_summary)
        if (item1 != null && item2 != null && item3 != null) {
            item1.isVisible = false
            item2.isVisible = false
            item3.isVisible = false
        }

        val shareButton = menu.findItem(R.id.shareButton)
        val deleteButton = menu.findItem(R.id.deleteButton)

        shareButton.isVisible = true
        deleteButton.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareButton -> onShareButtonClicked()
            R.id.deleteButton -> deleteImage()
            android.R.id.home -> cosplayController.navigateUp()
        }

        return true
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
        private val data: MutableList<String>,
    ) : FragmentStateAdapter(fragment) {

        fun removeAt(position: Int) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }

        fun getItem(position: Int): String {
            return data[position]
        }

        fun getDataSize(): Int {
            return data.size
        }

        override fun getItemCount(): Int = data.size

        override fun getItemId(position: Int): Long {
            return data[position].hashCode().toLong()
        }

        override fun createFragment(position: Int): Fragment {
            // println("Creating fragment: $position with path ${data[position].first}")
            val fragment = PictureItemFragment()
            fragment.arguments = Bundle().apply {
                putString(IMAGE_PATH, data[position])
            }
            return fragment
        }
    }
}