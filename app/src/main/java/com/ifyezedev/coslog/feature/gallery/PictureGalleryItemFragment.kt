package com.ifyezedev.coslog.feature.gallery

import android.os.Bundle
import android.view.View
import androidx.navigation.navGraphViewModels
import com.ifyezedev.coslog.CosplayActivity
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.core.common.usecase.OpenAndroidImageGallery
import com.ifyezedev.coslog.databinding.PictureItemBinding
import com.ifyezedev.coslog.data.db.CosLogDatabase
import com.ifyezedev.coslog.feature.elements.details.ElementsDetailsViewModel
import com.ifyezedev.coslog.feature.elements.details.ElementsViewModelFactory

class PictureGalleryItemFragment : BaseFragment<PictureItemBinding>() {
    override fun bindingLayoutId(): Int = R.layout.picture_item
    private lateinit var alag: ElementsDetailsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val path = arguments?.getString(PictureGalleryFragment.Keys.IMAGE_PATH)!!
        println(path)

        val controller = (requireActivity() as CosplayActivity).cosplayController
        val detailsViewModel: ElementsDetailsViewModel by navGraphViewModels(
            controller.previousBackStackEntry!!.destination.parent!!.id
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
        alag = detailsViewModel

        binding.imageView.setImageBitmap(detailsViewModel.imageData.value?.filter { it.first == path }!!
            .first().second)

        super.onViewCreated(view, savedInstanceState)
    }
}