package com.ifyezedev.coslog.feature.elements

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.ifyezedev.coslog.*
import com.ifyezedev.coslog.core.etc.BoundsOffsetDecoration
import com.ifyezedev.coslog.core.etc.SnapOnScrollListener
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.databinding.ElementBottomBinding
import com.ifyezedev.coslog.feature.elements.internal.*
import com.ifyezedev.coslog.feature.elements.internal.usecase.OpenAndroidImageGalleryUseCase


abstract class ElementsDetailsFragment<T : ViewDataBinding> : CosplayActivityBaseFragment<T>(),
    View.OnClickListener,
    MiniGalleryAdapter.OnClickListener {

    abstract override fun bindingLayoutId(): Int

    companion object {
        private const val BUNDLE_ITEM = "com.ifyezedev.coslog.keys.fragments.new_item"

        /**
         * When navigating to this fragment, we need to make sure that we pass this bundle, to
         * know whether to load or insert the data into the database.
         * */
        fun getNewItemBundle(element: Element?): Bundle = Bundle().apply {
            putParcelable(BUNDLE_ITEM, element)
        }
    }

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
        val createNewItem = arguments?.getParcelable<Element>(BUNDLE_ITEM)
        if (createNewItem == null) {
            setUpForNewItem()
        } else {
            setupToUpdateItem(createNewItem)
        }

        setup()
    }

    protected open fun setupToUpdateItem(element: Element) {
        bottomBinding.buttonDelete.visibility = View.VISIBLE
        bottomBinding.buttonsLayout.weightSum = 2F
        println(element)
    }

    protected open fun setUpForNewItem() {
        bottomBinding.buttonDelete.visibility = View.GONE
        bottomBinding.buttonsLayout.weightSum = 1F

        println("new item")
    }

    private fun setup() = bottomBinding.run {
        viewModelFactory = ElementsViewModel.ElementsViewModelFactory(
            OpenAndroidImageGalleryUseCase(),
            loadBitmapsFromInternalStorage,
            loadBitmapsFromAndroidGallery,
            saveBitmapsToInternalStorage,
            imageFileProvider,
        )

        viewModel = ViewModelProvider(
            viewModelStore,
            viewModelFactory)
            .get(ElementsViewModel::class.java)
        // setup buttons, recycler view, etc.
        val snapHelper: SnapHelper = PagerSnapHelper()

        buttonAddImage.setOnClickListener(this@ElementsDetailsFragment)
        buttonSave.setOnClickListener(this@ElementsDetailsFragment)
        buttonDelete.setOnClickListener(this@ElementsDetailsFragment)

        // snap helper helps by snapping the images from the image gallery recycler view
        // when scrolling them
        snapHelper.attachToRecyclerView(recyclerView)

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

        viewModel.loadedImagesAndPathsFromAndroidGallery.observe(cosplayController.currentBackStackEntry!!) {
            adapter.addAll(it)
        }
        // Whenever we load images by using viewModel.loadBitmapsFromInternalStorage() we
        // update our adapter to display the bitmaps and store their respective file paths.
        // This is usually updated when the fragment is created.
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
            // the result is observed through viewModel.loadedImagesAndPathsFromAndroidGallery
            viewModel.loadImagesFromAndroidGallery(intent)
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
        // We have to make sure that we clear the viewModelStore because when navigating
        // forward to any fragment, all view models get stored in the current associated fragment manager
        // and not being destroyed; this leads to subtle live data bugs like, for example, refreshing
        // some old data that was meant to be stored and updated only for configuration change in a
        // specific fragment.
        if (!requireActivity().isChangingConfigurations)
            viewModelStore.clear()
        super.onDestroyView()
    }
}


