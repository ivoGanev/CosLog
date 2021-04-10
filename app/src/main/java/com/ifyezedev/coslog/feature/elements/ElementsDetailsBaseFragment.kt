package com.ifyezedev.coslog.feature.elements

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.ifyezedev.coslog.CosplayBaseFragment
import com.ifyezedev.coslog.MiniGalleryAdapter
import com.ifyezedev.coslog.R
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

    private lateinit var bitmapUriCache: BitmapUriCache

    private lateinit var viewModel: ElementsViewModel
    private lateinit var viewModelFactory: ElementsViewModel.ElementsViewModelFactory

    private var currentSelectedImagePosition = RecyclerView.NO_POSITION

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomBinding =
            DataBindingUtil.bind(view.findViewById(R.id.bottomView))!!

        // setup buttons, recycler view, etc.
        setupViews()
    }

    private fun setupViews() = bottomBinding.run {
        val snapHelper: SnapHelper = PagerSnapHelper()
        bitmapUriCache = BitmapUriCache()

        buttonAddImage.setOnClickListener(this@ElementsDetailsBaseFragment)
        buttonSave.setOnClickListener(this@ElementsDetailsBaseFragment)
        buttonDelete.setOnClickListener(this@ElementsDetailsBaseFragment)
        snapHelper.attachToRecyclerView(recyclerView)

        viewModelFactory = ElementsViewModel.ElementsViewModelFactory(
            OpenAndroidImageGalleryUseCase(),
            DeleteBitmapFromInternalStorageUseCase(),
            LoadBitmapsFromInternalStorageUseCase(galleryTag),
            GetBitmapsFromAndroidGalleryUseCase(),
            SaveBitmapsToInternalStorageUseCase(),
        )

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ElementsViewModel::class.java)


        viewModel.loadBitmapsFromInternalStorage(requireContext())
        { bitmapHolders ->

            // Bitmap holders are loaded in through an IO dispatcher so we need to move
            // them on the Main dispatcher if we want to assign them to UI components.
            lifecycleScope.launch(Dispatchers.Main) {
                adapter = MiniGalleryAdapter(bitmapHolders.toMutableList())
                Log.e(this@ElementsDetailsBaseFragment::class.java.toString(),  "Hello")
                println(adapter)
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

        // TODO: filePath as null is ambiguous and error prone.
        val filePath = adapter.getCurrentSelectedItemFilePath()
        if(filePath!=null) {
            val removeSuccessful = adapter.removeItemAtCurrentSelectedPosition()
            if (removeSuccessful)
                viewModel.deleteBitmapFromInternalStorage(filePath)
        }
    }

    private fun onSaveButtonPressed() {
        val t=  galleryTag
        viewModel.saveBitmapsToInternalStorage(requireContext(), galleryTag)
    }

    private fun onAddImageButtonPressed() {
        viewModel.openAndroidImageGallery(::startActivityForResult)
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
        cosplayController.navigate(R.id.pictureViewerFragment)
    }
}


