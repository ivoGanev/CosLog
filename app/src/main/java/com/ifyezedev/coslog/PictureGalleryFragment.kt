package com.ifyezedev.coslog

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ifyezedev.coslog.PictureGalleryFragment.Keys.GALLERY_TAG
import com.ifyezedev.coslog.PictureGalleryFragment.Keys.IMAGE_INDEX
import com.ifyezedev.coslog.PictureGalleryFragment.Keys.IMAGE_PATH
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.databinding.FragmentPictureGalleryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class PictureGalleryFragment : BaseFragment<FragmentPictureGalleryBinding>() {

    object Keys {
        const val IMAGE_PATH = "com.ifyezedev.coslog.keys.fragments.image_path"
        const val IMAGE_INDEX = "com.ifyezedev.coslog.keys.fragments.image_index"
        const val GALLERY_TAG = "com.ifyezedev.coslog.keys.fragments.gallery_tag"
    }

    override fun bindingLayoutId(): Int = R.layout.fragment_picture_gallery

    // TODO: Move this to a ViewModel with LiveData eventually
    private var pagePosition: Int = -1

    private lateinit var imagePagerAdapter: ImagePagerAdapter

    override fun onStart() {
        super.onStart()
        val dir = File(context?.filesDir, arguments?.getString(GALLERY_TAG))
        val galleryData = dir.listFiles().mapIndexed { index, file ->

          //  println("Mapped ${file.path} to $index")
            Pair(file.path, index)
        }

        imagePagerAdapter = ImagePagerAdapter(
            this@PictureGalleryFragment,
            galleryData as MutableList<Pair<String, Int>>
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

    private fun deleteImage() {
        lifecycleScope.launch {
            val deletePath = imagePagerAdapter.data[pagePosition].first
            deleteBitmapsFromInternalStorageUseCase.invoke(deletePath)
            launch(Dispatchers.Main) {
                imagePagerAdapter.removeAt(pagePosition)
                updateToolbarTitle()

                if (imagePagerAdapter.data.isEmpty())
                {}
                //osplayController.popBackStack()
            }
        }
    }

    private fun updateToolbarTitle() {
        activity?.actionBar?.title = "Image: ${pagePosition + 1} out of ${imagePagerAdapter.data.size}"
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
        private val _data: MutableList<Pair<String, Int>>
    ) : FragmentStateAdapter(fragment) {

        val data: List<Pair<String, Int>> get() = _data

        fun removeAt(position: Int) {
            _data.removeAt(position)
            notifyItemRemoved(position)
        }

        override fun getItemCount(): Int = _data.size

        override fun getItemId(position: Int): Long {
            return _data[position].hashCode().toLong()
        }

        override fun createFragment(position: Int): Fragment {
            // println("Creating fragment: $position with path ${data[position].first}")
            val fragment = PictureItemFragment()
            fragment.arguments = Bundle().apply {
                putString(IMAGE_PATH, _data[position].first)
                putInt(IMAGE_INDEX, position + 1)
            }
            return fragment
        }
    }
}