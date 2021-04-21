package com.ifyezedev.coslog.feature.elements

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.ifyezedev.coslog.*
import com.ifyezedev.coslog.core.etc.BoundsOffsetDecoration
import com.ifyezedev.coslog.core.etc.SnapOnScrollListener
import com.ifyezedev.coslog.core.functional.onSuccess
import com.ifyezedev.coslog.databinding.ElementBottomBinding
import com.ifyezedev.coslog.feature.elements.internal.*
import com.ifyezedev.coslog.feature.elements.internal.usecase.OpenAndroidImageGalleryUseCase


abstract class ElementsDetailsFragment<T : ViewDataBinding> : CosplayActivityBaseFragment<T>(),
    View.OnClickListener,
    MiniGalleryAdapter.OnClickListener {

    abstract override fun bindingLayoutId(): Int

    /**
     * This is the bottom part of the layout that contains the save button,
     * the image gallery recycler view, and the notes.
     * */
    protected lateinit var bottomBinding: ElementBottomBinding

    private lateinit var adapter: MiniGalleryAdapter

    private lateinit var viewModel: ElementsViewModel
    private lateinit var viewModelFactory: ElementsViewModel.ElementsViewModelFactory

    override fun onAfterBindingCreated(view: View) {
        super.onAfterBindingCreated(view)
        bottomBinding =
            DataBindingUtil.bind(view.findViewById(R.id.bottomView))!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() = bottomBinding.run {
        // setup buttons, recycler view, etc.
        val snapHelper: SnapHelper = PagerSnapHelper()

        buttonAddImage.setOnClickListener(this@ElementsDetailsFragment)
        buttonSave.setOnClickListener(this@ElementsDetailsFragment)
        buttonDelete.setOnClickListener(this@ElementsDetailsFragment)

        // snap helper helps by snapping the images from the image gallery recycler view
        // when scrolling them
        snapHelper.attachToRecyclerView(recyclerView)

        viewModelFactory = ElementsViewModel.ElementsViewModelFactory(
            OpenAndroidImageGalleryUseCase(),
            loadBitmapsFromInternalStorage,
            loadBitmapsFromAndroidGallery,
            saveBitmapsToInternalStorage,
            imageFileProvider,
        )

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ElementsViewModel::class.java)

        adapter = MiniGalleryAdapter()
        // set stable ids to make sure we have animations when adding images
        adapter.setHasStableIds(true)

        bottomBinding.recyclerView.adapter = adapter
        adapter.clickListener = this@ElementsDetailsFragment

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.addItemDecoration(BoundsOffsetDecoration())
        recyclerView.addOnScrollListener(
            SnapOnScrollListener(
                snapHelper,
                SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
                adapter.onSnapPositionChangeListener
            )
        )

        // Whenever we load images from the viewModel.loadBitmapsFromInternalStorage() we
        // update our adapter to display the bitmaps. This is usually updated when the fragment
        // starts.
        viewModel.loadBitmapsFromInternalStorage {
            adapter.addAll(it)
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
        // TODO: delete logic
    }

    private fun onSaveButtonPressed() {
        viewModel.saveBitmapsToInternalStorage(adapter.getData())
        toastNotify("Successfully saved images to internal storage.")
    }

    private fun onAddImageButtonPressed() {
        // here we delegate the entire business logic to the view model.
        viewModel.openAndroidImageGalleryForResult(::startActivityForResult)
    }

    // called when we pick an image from the gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode == Activity.RESULT_OK &&
            requestCode == 0 &&
            intent != null
        ) {
            // update loaded images from android gallery
            viewModel.loadImagesFromAndroidGallery(intent) {
                adapter.addAll(it)
            }
        }
    }

    override fun onImageClickedListener(view: View) {
        val bundle = Bundle().apply {
            // this is telling the PictureGalleryFragment, the one we are about to navigate to,
            // on which item position it should move to.
            putInt(PictureGalleryFragment.Keys.IMAGE_INDEX, adapter.currentSelectedImagePosition)
        }
        cosplayController.navigate(R.id.pictureViewerFragment, bundle)
    }

    override fun onDestroyView() {
        adapter.clear()
        super.onDestroyView()
    }
}


