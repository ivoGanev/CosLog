package com.ifyezedev.coslog.feature.elements

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
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
        println(savedInstanceState)
        bottomBinding =
            DataBindingUtil.bind(view.findViewById(R.id.bottomView))!!

        // setup buttons, recycler view, etc.
        setupViews()

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

        // The details page use a pending uri bitmap cache to store only the
        // Uri path instead of the actual Bitmaps.
        // Due to inheritance unfortunately, both details page fragments share
        // the same view model and access the same cache. We need to clean it when
        // the we navigate away from any of them to prevent one of them using the
        // other one's cache which results in a bug.
        if(savedInstanceState==null) {
            viewModel.clearPendingUriCache()
             //println("cache cleared")
        }
        else {
            viewModel.loadBitmapsFromCachedUris(requireContext())
            { bitmapHolders ->

                // Bitmap holders are loaded through an IO dispatcher so we need to move
                // them on the Main dispatcher if we want to assign them to UI components.
                lifecycleScope.launch(Dispatchers.Main) {
                    adapter.addAll(bitmapHolders)
                }
            }
        }
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

                    // bug: if the scrolling is slow it will flicker because notifyDataSetChanged()
                    // will redraw the whole RV
                    adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
                        override fun onChanged() {
                            val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
                                private val MILLISECONDS_PER_INCH = 10f // The bigger the value, the slower

                                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                                    return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
                                }
                            }
                            smoothScroller.targetPosition = adapter.data.size
                            bottomBinding.recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
                        }
                    })

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


