package com.ifyezedev.coslog

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.databinding.ElementBottomBinding

//TODO: when rotating the app obviously the bitmapUriCache is not
//  saved and picture are removed from the recycler view. This eventually
//  should be fixed with a view model.
abstract class ElementsBasePagerFragment<T : ViewDataBinding> : CosplayBaseFragment<T>(), View.OnClickListener {

    abstract val galleryTag: String
    abstract override fun bindingLayoutId(): Int

    private lateinit var bottomBinding: ElementBottomBinding

    private val bitmapUriCache: MutableList<Uri> = mutableListOf()

    private lateinit var adapter: MiniGalleryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println(view.findViewById(R.id.bottomView))
        bottomBinding =
            DataBindingUtil.bind(view.findViewById(R.id.bottomView))!!

        val snapHelper: SnapHelper = PagerSnapHelper()
        val files = bitmapResolver.openAll(galleryTag) as MutableList

        adapter = MiniGalleryAdapter(files)

        bottomBinding.apply {
            buttonAddImage.setOnClickListener(this@ElementsBasePagerFragment)
            buttonSave.setOnClickListener(this@ElementsBasePagerFragment)
            recyclerView.adapter = adapter
            snapHelper.attachToRecyclerView(recyclerView)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonAddImage -> onAddImage()
            R.id.buttonSave -> onSaveButtonPressed()
        }
    }

    private fun onSaveButtonPressed() {
        bitmapResolver.saveAll(bitmapUriCache, "buy-gallery")
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
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            val tempCache: MutableList<Uri> = mutableListOf()

            intent?.data?.let { uri -> tempCache.add(uri) }
            intent?.clipData?.let { clipData -> tempCache.addAll(clipData.mapToUri()) }

            val bitmapImages = bitmapResolver.open(tempCache)
            bitmapUriCache.addAll(tempCache)
            adapter.addAll(bitmapImages)
        }
    }

    private fun ClipData.mapToUri(): MutableList<Uri> {
        val result = mutableListOf<Uri>()
        for (i in 0 until this.itemCount) {
            result.add(this.getItemAt(i).uri)
        }
        return result
    }
}

