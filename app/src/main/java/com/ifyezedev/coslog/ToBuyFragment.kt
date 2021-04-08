package com.ifyezedev.coslog

import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.databinding.FragmentToBuyBinding
import com.ifyezedev.coslog.databinding.PictureItemBinding


class ToBuyFragment : CosplayBaseFragment<FragmentToBuyBinding>(), View.OnClickListener {
    private val galleryTag = "to-buy"

    override fun bindingLayoutId(): Int = R.layout.fragment_to_buy

    private val bitmapUriCache: MutableList<Uri> = mutableListOf()

    private lateinit var adapter: Adapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val snapHelper: SnapHelper = PagerSnapHelper()
        val files = appBitmapHandler.openAll(galleryTag) as MutableList

        adapter = Adapter(files)

        binding {
            bottom.buttonAddImage.setOnClickListener(this@ToBuyFragment)
            bottom.buttonSave.setOnClickListener(this@ToBuyFragment)
            bottom.recyclerView.adapter = adapter
            snapHelper.attachToRecyclerView(bottom.recyclerView)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonAddImage -> onAddImage()
            R.id.buttonSave -> onSaveButtonPressed()
        }
    }

    private fun onSaveButtonPressed() {
        appBitmapHandler.saveAll(bitmapUriCache, "buy-gallery")
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
        if (resultCode == RESULT_OK && requestCode == 0) {
            val tempCache: MutableList<Uri> = mutableListOf()

            intent?.data?.let { uri -> tempCache.add(uri) }
            intent?.clipData?.let { clipData -> tempCache.addAll(clipData.mapUri()) }

            val bitmapImages = appBitmapHandler.open(tempCache)
            bitmapUriCache.addAll(tempCache)
            adapter.addAll(bitmapImages)
        }
    }

    private class Adapter(private val data: MutableList<Bitmap>) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {

        fun addAll(data: List<Bitmap>) {
            this.data.addAll(data)
            this.notifyDataSetChanged()
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val binding: PictureItemBinding = PictureItemBinding.bind(itemView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.picture_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val bitmap = data[position] as Bitmap
            holder.binding.imageView.setImageBitmap(bitmap)
        }
    }

    private fun ClipData.mapUri(): MutableList<Uri> {
        val result = mutableListOf<Uri>()
        for (i in 0 until this.itemCount) {
            result.add(this.getItemAt(i).uri)
        }
        return result
    }
}

