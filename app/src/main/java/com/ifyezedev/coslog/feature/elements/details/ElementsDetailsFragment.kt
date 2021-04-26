package com.ifyezedev.coslog.feature.elements.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.ifyezedev.coslog.*
import com.ifyezedev.coslog.core.etc.BoundsOffsetDecoration
import com.ifyezedev.coslog.core.etc.SnapOnScrollListener
import com.ifyezedev.coslog.data.db.CosLogDatabase
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.databinding.ElementBottomBinding
import com.ifyezedev.coslog.feature.elements.internal.*
import com.ifyezedev.coslog.core.common.usecase.OpenAndroidImageGallery


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

    protected lateinit var adapter: MiniGalleryAdapter

    protected lateinit var detailsViewModel: ElementsDetailsViewModel

    private lateinit var detailsViewModelFactory: ElementsDetailsViewModel.ElementsViewModelFactory

    protected var element: Element? = null

    override fun onAfterBindingCreated(view: View) {
        super.onAfterBindingCreated(view)
        bottomBinding =
            DataBindingUtil.bind(view.findViewById(R.id.bottomView))!!
        element = arguments?.getParcelable(BUNDLE_ITEM)

        detailsViewModelFactory = ElementsDetailsViewModel.ElementsViewModelFactory(
            OpenAndroidImageGallery(),
            loadBitmapsFromInternalStorage,
            loadBitmapsFromAndroidGallery,
            saveBitmapsToInternalStorage,
            imageFilePathProvider,
            CosLogDatabase.getDatabase(requireContext()).cosLogDao
        )

        detailsViewModel = ViewModelProvider(
            viewModelStore,
            detailsViewModelFactory)
            .get(ElementsDetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (element == null) {
            initializeEmpty()
        } else {
            initializeWithElement(element!!)
        }

        initialize()
    }

    @CallSuper
    protected open fun initializeWithElement(element: Element) {
        bottomBinding.buttonDelete.visibility = View.VISIBLE
        bottomBinding.buttonsLayout.weightSum = 2F
    }

    @CallSuper
    protected open fun initializeEmpty() {
        bottomBinding.buttonDelete.visibility = View.GONE
        bottomBinding.buttonsLayout.weightSum = 1F
    }

    private fun initialize() = bottomBinding.run {

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

        detailsViewModel.preparedPicturePaths.observe(viewLifecycleOwner) {
            detailsViewModel.loadCachedPicturePathsWithElement(element) {
                adapter.setData(it)
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
        detailsViewModel.deleteElementFromDatabase(element)
        cosplayController.navigateUp()
    }

    protected open fun onSaveButtonPressed() {
    }

    private fun onAddImageButtonPressed() {
        // here we delegate the entire business logic to the view model.
        detailsViewModel.openAndroidImageGalleryForResult(::startActivityForResult)
    }

    // called when we pick an image from the gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode == Activity.RESULT_OK &&
            requestCode == 0 &&
            intent != null
        ) {
            // the result is observed through viewModel.loadedImagesAndPathsFromAndroidGallery
            detailsViewModel.prepareImagesFromAndroidGalleryForLoading(intent)
        }
    }

    /**
     * This gets executed when a picture from the mini image gallery is clicked.
     * */
    override fun onPictureClicked(view: View) {
        val bundle = Bundle().apply {
            // this is telling the PictureGalleryFragment, the one we are about to navigate to,
            // on which item position it should move to.
            putInt(PictureGalleryFragment.Keys.IMAGE_INDEX, adapter.currentSelectedImagePosition)
            putStringArrayList(PictureGalleryFragment.Keys.IMAGE_PATH,
                ArrayList(adapter.getFilePaths()))
        }
        cosplayController.navigate(R.id.pictureViewerFragment, bundle)
    }
}


