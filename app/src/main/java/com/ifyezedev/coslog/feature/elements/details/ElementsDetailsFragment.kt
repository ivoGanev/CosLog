package com.ifyezedev.coslog.feature.elements.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.*
import com.ifyezedev.coslog.*
import com.ifyezedev.coslog.core.etc.BoundsOffsetDecoration
import com.ifyezedev.coslog.core.etc.SnapOnScrollListener
import com.ifyezedev.coslog.data.db.CosLogDatabase
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.databinding.ElementBottomBinding
import com.ifyezedev.coslog.core.common.usecase.OpenAndroidImageGallery
import com.ifyezedev.coslog.feature.gallery.PictureGalleryFragment


abstract class ElementsDetailsFragment<T : ViewDataBinding> : CosplayActivityBaseFragment<T>(),
    View.OnClickListener,
    ElementsGalleryAdapter.OnClickListener {

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

    protected lateinit var adapter: ElementsGalleryAdapter

    protected abstract val sourceNavGraphId: Int

    protected val detailsViewModel: ElementsDetailsViewModel by navGraphViewModels(sourceNavGraphId) {
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

    private lateinit var detailsViewModelFactory: ElementsViewModelFactory

    protected val element: Element? by lazy {
        arguments?.getParcelable(BUNDLE_ITEM)
    }

    protected val willInsertNewElement get() = element == null

    override fun onAfterBindingCreated(view: View) {
        super.onAfterBindingCreated(view)
        bottomBinding =
            DataBindingUtil.bind(view.findViewById(R.id.bottomView))!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (willInsertNewElement) {
            onInsertNewElement()
        } else {
            onUpdateElement(element!!)
        }
        initialize()
    }

    /**
     * This method will be called on fragment start when an element is to be updated.
     * */
    protected open fun onUpdateElement(element: Element) {
    }

    /**
     * This method will be called on fragment start when a new element is to be added.
     * */
    protected open fun onInsertNewElement() {
    }

    private fun initialize() = bottomBinding.run {
        val snapHelper: SnapHelper = PagerSnapHelper()

        buttonAddImage.setOnClickListener(this@ElementsDetailsFragment)

        // snap helper helps by snapping the images from the image gallery recycler view
        // when scrolling them
        snapHelper.attachToRecyclerView(recyclerView)

        adapter = ElementsGalleryAdapter()
        // set stable ids to make sure we have animations when adding images
        adapter.setHasStableIds(true)

        bottomBinding.recyclerView.adapter = adapter
        adapter.clickListener = this@ElementsDetailsFragment

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerView.addOnScrollListener(
            SnapOnScrollListener(
                snapHelper,
                SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
                adapter.onSnapPositionChangeListener
            )
        )

        // we need to post this in order all the view dimensions to be calculated
        binding.root.post {
            // the recycler view items are positioned to the most left,but this makes them to start
            // in the middle.
            recyclerView.addItemDecoration(BoundsOffsetDecoration())
        }

        detailsViewModel.loadAllImagesFromElement(element)
        detailsViewModel.imageData.observe(viewLifecycleOwner) { imageData->
            adapter.setData(imageData)
            // make sure to update the element when we do something with the images
            //element?.images = ArrayList(imageData.map { it.first })
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        hideOverflowMenu(menu)

        val saveButton = menu.findItem(R.id.saveButton)
        saveButton.isVisible = true

        if (!willInsertNewElement) {
            val deleteButton = menu.findItem(R.id.deleteButton)
            deleteButton.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveButton -> onSaveButtonPressed()
            R.id.deleteButton -> onDeleteButtonPressed()
            android.R.id.home -> onBackButtonPressed()
        }
        return true
    }

    private fun onBackButtonPressed() {
        cosplayController.navigateUp()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonAddImage -> onAddImageButtonPressed()
        }
    }

    private fun onDeleteButtonPressed() {
        detailsViewModel.deleteElement(element)
        //  detailsViewModel.deleteAdapterFiles(adapter)
        cosplayController.navigateUp()
    }

    protected open fun onSaveButtonPressed() {
        detailsViewModel.clearPendingBitmapCache()
        cosplayController.navigateUp()
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

            requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            // the result is observed through viewModel.loadedImagesAndPathsFromAndroidGallery
            detailsViewModel.addImageDataFromIntent(intent) {
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
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
            putInt(PictureGalleryFragment.Keys.SOURCE_NAVIGATION_GRAPH_ID, sourceNavGraphId)
            putStringArrayList(PictureGalleryFragment.Keys.IMAGE_PATH,
                ArrayList(adapter.getFilePaths()))
        }
        cosplayController.navigate(R.id.pictureViewerFragment, bundle)
    }
}


