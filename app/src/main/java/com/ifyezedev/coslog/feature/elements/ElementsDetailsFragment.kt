package com.ifyezedev.coslog.feature.elements

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.ifyezedev.coslog.*
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.core.etc.BoundsOffsetDecoration
import com.ifyezedev.coslog.core.etc.SnapOnScrollListener
import com.ifyezedev.coslog.core.functional.onSuccess
import com.ifyezedev.coslog.databinding.ElementBottomBinding
import com.ifyezedev.coslog.feature.elements.internal.*
import com.ifyezedev.coslog.feature.elements.internal.usecase.OpenAndroidImageGalleryUseCase


abstract class ElementsDetailsFragment<T : ViewDataBinding> : BaseFragment<T>(),
    View.OnClickListener,
    MiniGalleryAdapter.OnClickListener {

    open val galleryTag: String = "image-gallery"

    abstract override fun bindingLayoutId(): Int

    lateinit var bottomBinding: ElementBottomBinding

    private lateinit var adapter: MiniGalleryAdapter

    private lateinit var viewModel: ElementsViewModel
    private lateinit var viewModelFactory: ElementsViewModel.ElementsViewModelFactory

    override fun onAfterBindingCreated(view: View) {
        bottomBinding =
            DataBindingUtil.bind(view.findViewById(R.id.bottomView))!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup buttons, recycler view, etc.
        setupViews()

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ElementsViewModel::class.java)
    }

    private fun setupViews() = bottomBinding.run {
        val snapHelper: SnapHelper = PagerSnapHelper()

        buttonAddImage.setOnClickListener(this@ElementsDetailsFragment)
        buttonSave.setOnClickListener(this@ElementsDetailsFragment)
        buttonDelete.setOnClickListener(this@ElementsDetailsFragment)
        snapHelper.attachToRecyclerView(recyclerView)

        viewModelFactory = ElementsViewModel.ElementsViewModelFactory(
            OpenAndroidImageGalleryUseCase(),
            loadBitmapsFromInternalStorage,
            loadBitmapsFromAndroidGallery,
            saveBitmapsToInternalStorage,
            imageFileProvider,
        )

        adapter =
            MiniGalleryAdapter(
                lifecycleScope,
                loadBitmapsFromAndroidGallery,
                loadBitmapsFromInternalStorage)
        adapter.setHasStableIds(true)

        bottomBinding.recyclerView.adapter = adapter
        adapter.clickListener = this@ElementsDetailsFragment

        recyclerView.layoutManager = MyLayoutManager(requireContext())
        recyclerView.addItemDecoration(BoundsOffsetDecoration())
        recyclerView.addOnScrollListener(
            SnapOnScrollListener(
                snapHelper,
                SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
                adapter.onSnapPositionChangeListener
            )
        )

        imageFileProvider.getInternalStorageImageFilePaths()?.let { pathsToInternalStorageImages ->
            loadBitmapsFromInternalStorage(lifecycleScope,
                pathsToInternalStorageImages) { bitmapResult ->
                bitmapResult.onSuccess { internalStorageBitmaps ->
                    adapter.addAll(pathsToInternalStorageImages.zip(internalStorageBitmaps))
                }
            }
        }
    }


    class MyLayoutManager(context: Context?) : LinearLayoutManager(context, HORIZONTAL, false) {
        override fun supportsPredictiveItemAnimations(): Boolean {
            return true
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonAddImage -> onAddImageButtonPressed()
            R.id.buttonSave -> onSaveButtonPressed()
            R.id.buttonDelete -> onDeleteButtonPressed()
        }
    }

    private fun onDeleteButtonPressed() {
    }

    private fun onSaveButtonPressed() {
        viewModel.saveBitmapsToInternalStorageFromCache(adapter.getData())
        toastNotify("Successfully saved images to internal storage.")
    }

    private fun onAddImageButtonPressed() {
        viewModel.openAndroidImageGalleryForResult(::startActivityForResult)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode == Activity.RESULT_OK &&
            requestCode == 0 &&
            intent != null
        ) {
            viewModel.onFetchImagesFromGallery(intent) { uris ->
                loadBitmapsFromAndroidGallery(lifecycleScope, uris) { result ->
                    result.onSuccess { bitmaps ->
                        adapter.addAll(uris.map { it.toString() }.zip(bitmaps))
                    }
                }
            }
        }
    }

    override fun onImageClickedListener(view: View) {
        val bundle = Bundle().apply {
            putInt(PictureGalleryFragment.Keys.IMAGE_INDEX, adapter.currentSelectedImagePosition)
            putString(PictureGalleryFragment.Keys.GALLERY_TAG, galleryTag)
        }
        val cosplayController = (activity as CosplayActivity).cosplayController

        cosplayController.navigate(R.id.pictureViewerFragment, bundle)
    }
}


