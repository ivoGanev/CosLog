package com.ifyezedev.coslog.feature.elements

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.ifyezedev.coslog.*
import com.ifyezedev.coslog.core.etc.BoundsOffsetDecoration
import com.ifyezedev.coslog.core.etc.SnapOnScrollListener
import com.ifyezedev.coslog.databinding.ElementBottomBinding
import com.ifyezedev.coslog.feature.elements.internal.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class ElementsDetailsBaseFragment<T : ViewDataBinding> : CosplayBaseFragment<T>(),
    View.OnClickListener,
    MiniGalleryAdapter.OnClickListener {

    abstract val galleryTag: String

    abstract override fun bindingLayoutId(): Int

    private lateinit var bottomBinding: ElementBottomBinding

    private lateinit var adapter: MiniGalleryAdapter

    private lateinit var viewModel: ElementsViewModel
    private lateinit var viewModelFactory: ElementsViewModel.ElementsViewModelFactory

    private var navigatedIntoFragment: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomBinding =
            DataBindingUtil.bind(view.findViewById(R.id.bottomView))!!

        // setup buttons, recycler view, etc.
        setupViews()
        var navigated = false

        val a = requireActivity() as CosplayActivity
        a.onNavAway = CosplayActivity.OnNavigatedAway {
            if (savedInstanceState != null) {
                navigated = savedInstanceState.getBoolean("navigated")
            }
            if (navigated) {
                viewModel.clearPendingUriCache()
                a.onNavAway = null
                // println("cache cleared")
            }
            navigated = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("navigated", true)
        super.onSaveInstanceState(outState)
    }

    private fun setupViews() = bottomBinding.run {
        val snapHelper: SnapHelper = PagerSnapHelper()

        buttonAddImage.setOnClickListener(this@ElementsDetailsBaseFragment)
        buttonSave.setOnClickListener(this@ElementsDetailsBaseFragment)
        buttonDelete.setOnClickListener(this@ElementsDetailsBaseFragment)
        snapHelper.attachToRecyclerView(recyclerView)

        viewModelFactory = ElementsViewModel.ElementsViewModelFactory(
            OpenAndroidImageGalleryUseCase(),
            DeleteBitmapFromInternalStorageUseCase(),
            LoadBitmapsFromInternalStorageUseCase(),
            GetBitmapsFromAndroidGalleryUseCase(),
            SaveBitmapsToInternalStorageUseCase(),
        )

        adapter = MiniGalleryAdapter(mutableListOf())
        bottomBinding.recyclerView.adapter = adapter
        adapter.clickListener = this@ElementsDetailsBaseFragment

        recyclerView.addItemDecoration(BoundsOffsetDecoration())
        recyclerView.addOnScrollListener(
            SnapOnScrollListener(
                snapHelper,
                SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
                adapter.onSnapPositionChangeListener
            )
        )

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ElementsViewModel::class.java)

        viewModel.loadBitmapsFromInternalStorage(requireContext(), galleryTag)
        { bitmapHolders ->

            // Bitmap holders are loaded through an IO dispatcher so we need to move
            // them on the Main dispatcher if we want to assign them to UI components.
            lifecycleScope.launch(Dispatchers.Main) {
                adapter.addAll(bitmapHolders)
            }
        }

        viewModel.loadBitmapsFromCachedUris(requireContext())
        { bitmapHolders ->

            // Bitmap holders are loaded through an IO dispatcher so we need to move
            // them on the Main dispatcher if we want to assign them to UI components.
            lifecycleScope.launch(Dispatchers.Main) {
                adapter.addAll(bitmapHolders)
            }
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
        // TODO: Check if the user is able to cancel the delete action

        // TODO: filePath as null is ambiguous and error prone. Need to rethink the design.
        val filePath = adapter.getCurrentSelectedItemFilePath()
        if (filePath != null) {
            val removeSuccessful = adapter.removeItemAtCurrentSelectedPosition()
            if (removeSuccessful)
                viewModel.deleteBitmapFromInternalStorage(filePath)
        }
        toastNotify("Image deleted from internal storage.")
    }

    private fun onSaveButtonPressed() {
        viewModel.saveBitmapsToInternalStorage(requireContext(), galleryTag)
        // TODO: Make sure to handle IOExceptions
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
            viewModel.getBitmapsFromAndroidGallery(requireContext(), intent) { bitmapHolders ->
                lifecycleScope.launch(Dispatchers.Main) {
                    adapter.addAll(bitmapHolders)
                }
            }
        }
    }

    override fun onImageClickedListener(view: View) {
        val bundle = Bundle().apply {
            putInt(PictureGalleryFragment.Keys.IMAGE_INDEX, adapter.currentSelectedImagePosition)
            putString(PictureGalleryFragment.Keys.GALLERY_TAG, galleryTag)
        }
        cosplayController.navigate(R.id.pictureViewerFragment, bundle)
    }
}


