package com.ifyezedev.coslog.feature.gallery

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.navGraphViewModels
import androidx.viewpager2.widget.ViewPager2
import com.ifyezedev.coslog.CosplayActivityBaseFragment
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.feature.gallery.PictureGalleryFragment.Keys.IMAGE_INDEX
import com.ifyezedev.coslog.feature.gallery.PictureGalleryFragment.Keys.IMAGE_PATH
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.core.common.usecase.OpenAndroidImageGallery
import com.ifyezedev.coslog.core.functional.onFailure
import com.ifyezedev.coslog.core.functional.onSuccess
import com.ifyezedev.coslog.data.db.CosLogDatabase
import com.ifyezedev.coslog.databinding.FragmentPictureGalleryBinding
import com.ifyezedev.coslog.feature.elements.details.ElementsDetailsViewModel
import com.ifyezedev.coslog.feature.elements.details.ElementsViewModelFactory

class PictureGalleryFragment : CosplayActivityBaseFragment<FragmentPictureGalleryBinding>() {

    object Keys {
        const val SOURCE_NAVIGATION_GRAPH_ID = "com.ifyezedev.coslog.keys.fragments.source_graph_id"
        const val IMAGE_PATH = "com.ifyezedev.coslog.keys.fragments.image_path"
        const val IMAGE_INDEX = "com.ifyezedev.coslog.keys.fragments.image_index"
    }

    override fun bindingLayoutId(): Int = R.layout.fragment_picture_gallery

    // TODO: Move this to a ViewModel with LiveData eventually
    private var pagePosition: Int = -1

    private lateinit var pictureGalleryPagerAdapter: PictureGalleryPagerAdapter

    private lateinit var detailsViewModel: ElementsDetailsViewModel
//    override fun onAfterBindingCreated(view: View) {
//        super.onAfterBindingCreated(view)
//        setHasOptionsMenu(true);
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _detailsViewModel: ElementsDetailsViewModel by navGraphViewModels(
            cosplayController.previousBackStackEntry!!.destination.parent!!.id
        ) {
            ElementsViewModelFactory(
                OpenAndroidImageGallery(),
                loadBitmapsFromInternalStorage,
                loadBitmapsFromAndroidGallery,
                saveBitmapsToInternalStorage,
                imageFilePathProvider,
                deleteFilesFromInternalStorage,
                CosLogDatabase.getDatabase(requireContext()).cosLogDao
            )
        }
        this.detailsViewModel = _detailsViewModel
    }

    override fun onStart() {
        super.onStart()
        val internalStorageImagePaths = arguments?.getStringArrayList(IMAGE_PATH)

        // if there are any images to display from internal storage
        if (internalStorageImagePaths != null) {
            pictureGalleryPagerAdapter = PictureGalleryPagerAdapter(
                this@PictureGalleryFragment,
                internalStorageImagePaths.toMutableList()
            )

            val imageIndex = requireArguments().getInt(IMAGE_INDEX)
            pagePosition = imageIndex

            binding {
                imagePager.adapter = pictureGalleryPagerAdapter
                imagePager.setCurrentItem(imageIndex, false)

                imagePager.registerOnPageChangeCallback(object :
                    ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        pagePosition = position
                        updateToolbarTitle()
                    }
                })
            }

            updateToolbarTitle()
        }
        // otherwise make the user aware that there are no images in the gallery
        else {
        }
    }

    private fun deleteImage() {
        detailsViewModel.deleteImage(pagePosition) { deleteImageResult ->
            deleteImageResult.onSuccess {
                pictureGalleryPagerAdapter.removeAt(pagePosition)
                if(pictureGalleryPagerAdapter.getSize() == 0) {
                    cosplayController.navigateUp()
                }
                else {
                    updateToolbarTitle()
                }
            }
            deleteImageResult.onFailure {
                toastNotify("Failed to delete image with path: ${detailsViewModel.imageData.value?.get(pagePosition)?.first}")
            }
        }
    }

    private fun updateToolbarTitle() {
        actionBar.title =
            "Image: ${pagePosition + 1} out of ${pictureGalleryPagerAdapter.getSize()}"
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        hideOverflowMenu(menu)

        val shareButton = menu.findItem(R.id.shareButton)
        val deleteButton = menu.findItem(R.id.deleteButton)

        shareButton.isVisible = true
        deleteButton.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareButton -> onShareButtonClicked()
            R.id.deleteButton -> deleteImage()
            else -> return super.onOptionsItemSelected(item)
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
}