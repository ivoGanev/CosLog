package com.ifyezedev.coslog.feature.elements

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.ifyezedev.coslog.CosplayBaseFragment
import com.ifyezedev.coslog.MiniGalleryAdapter
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.core.data.BitmapHolder
import com.ifyezedev.coslog.core.etc.BoundsOffsetDecoration
import com.ifyezedev.coslog.core.etc.OnSnapPositionChangeListener
import com.ifyezedev.coslog.core.etc.SnapOnScrollListener
import com.ifyezedev.coslog.databinding.ElementBottomBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ElementsBaseFragment<T : ViewDataBinding> : CosplayBaseFragment<T>(),
    View.OnClickListener,
    MiniGalleryAdapter.OnClickListener {

    abstract val galleryTag: String

    abstract override fun bindingLayoutId(): Int

    private lateinit var bottomBinding: ElementBottomBinding

    private lateinit var adapter: MiniGalleryAdapter

    private lateinit var getBitmapFromAndroidGalleryUseCase: GetBitmapFromAndroidGalleryUseCase

    private lateinit var loadBitmapsFromInternalStorageUseCase: LoadBitmapsFromInternalStorageUseCase

    private lateinit var saveBitmapstoInternalStorageUseCase: SaveBitmapToInternalStorageUseCase

    private lateinit var deleteBitmapsFromInternalStorageUseCase: DeleteBitmapFromInternalStorageUseCase

    private lateinit var bitmapHolderCache: BitmapHolderCache

    private var currentSelectedImagePosition = RecyclerView.NO_POSITION

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomBinding =
            DataBindingUtil.bind(view.findViewById(R.id.bottomView))!!

        injectClasses()
        // setup buttons and views
        setupViews()
    }

    private fun setupViews() = bottomBinding.run {
        val snapHelper: SnapHelper = PagerSnapHelper()

        val onSnapPositionChangeListener = OnSnapPositionChangeListener { position ->
            currentSelectedImagePosition = position
        }

        buttonAddImage.setOnClickListener(this@ElementsBaseFragment)
        buttonSave.setOnClickListener(this@ElementsBaseFragment)
        buttonDelete.setOnClickListener(this@ElementsBaseFragment)
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(BoundsOffsetDecoration())
        recyclerView.addOnScrollListener(
            SnapOnScrollListener(
                snapHelper,
                SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
                onSnapPositionChangeListener
            )
        )
    }

    private fun injectClasses() {
        bitmapHolderCache = BitmapHolderCache()

        getBitmapFromAndroidGalleryUseCase = GetBitmapFromAndroidGalleryUseCase(bitmapHolderCache)

        loadBitmapsFromInternalStorageUseCase =
            LoadBitmapsFromInternalStorageUseCase(galleryTag)

        saveBitmapstoInternalStorageUseCase = SaveBitmapToInternalStorageUseCase()

        deleteBitmapsFromInternalStorageUseCase = DeleteBitmapFromInternalStorageUseCase()

        lifecycleScope.launch {
            loadBitmapsFromInternalStorageUseCase.invoke(requireContext()) { bitmapHolders ->
                launch {
                    initGallery(bitmapHolders)
                }
            }
        }
    }

    private suspend fun initGallery(bitmapHolders: List<BitmapHolder>) =
        withContext(Dispatchers.Main) {
            adapter = MiniGalleryAdapter(bitmapHolders.toMutableList())
            bottomBinding.recyclerView.adapter = adapter
            adapter.clickListener = this@ElementsBaseFragment
        }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonAddImage -> onAddImage()
            R.id.buttonSave -> onSaveButtonPressed()
            R.id.buttonDelete -> onDeleteButtonPressed()
        }
    }

    private fun onDeleteButtonPressed() {
        if (currentSelectedImagePosition != RecyclerView.NO_POSITION) {
            // TODO: Check if this needs to be fixed so the user is able to cancel the delete action
            deleteBitmapsFromInternalStorageUseCase.invoke(listOf(adapter.data[currentSelectedImagePosition]))
            adapter.data.removeAt(currentSelectedImagePosition)
            adapter.notifyDataSetChanged()
        }
    }

    private fun onSaveButtonPressed() {
        val context = requireContext()
        val bitmapHolders = bitmapHolderCache.toBitmapHolders(context)

        lifecycleScope.launch {
            saveBitmapstoInternalStorageUseCase.invoke(context, bitmapHolders, galleryTag)
        }
    }

    private fun onAddImage() {
        val intent = buildIntent() {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
            putExtra("return-data", true);
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode == Activity.RESULT_OK &&
            requestCode == 0 &&
            intent != null
        ) {
            lifecycleScope.launch {
                getBitmapFromAndroidGalleryUseCase.invoke(
                    requireContext(),
                    intent
                ) { bitmapHolders ->
                    lifecycleScope.launch(Dispatchers.Main) {
                        adapter.addAll(bitmapHolders)
                    }
                }
            }
        }
    }

    override fun onImageClickedListener(view: View) {
        cosplayController.navigate(R.id.pictureViewerFragment)
    }
}

