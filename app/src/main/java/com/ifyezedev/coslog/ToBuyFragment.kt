package com.ifyezedev.coslog

import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.core.io.BitmapDetails
import com.ifyezedev.coslog.databinding.FragmentToBuyBinding
import com.ifyezedev.coslog.databinding.PictureItemBinding
import kotlin.math.roundToInt


class ToBuyFragment : CosplayBaseFragment<FragmentToBuyBinding>(), View.OnClickListener {
    private val galleryTag = "to-buy"

    override fun bindingLayoutId(): Int = R.layout.fragment_to_buy

    private val adapterData: MutableList<Pair<Bitmap, BitmapDetails>> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val snapHelper: SnapHelper = PagerSnapHelper()
        val files = appBitmapStore.openAll(galleryTag)
        val bitmapData = files.map { bitmap ->
            Pair(bitmap, BitmapDetails("", ""))
        }

        binding {
            bottom.buttonAddImage.setOnClickListener(this@ToBuyFragment)
            bottom.buttonSave.setOnClickListener(this@ToBuyFragment)
            bottom.recyclerView.adapter = Adapter(bitmapData)
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
        appBitmapStore.saveAll(adapterData)
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
            // Handle single files
            val imagesUri: MutableList<Uri> = mutableListOf()
            intent?.let {
            }
            intent?.data?.let { uri -> imagesUri.add(uri) }
            intent?.clipData?.let { clipData -> imagesUri.addAll(clipData.mapUri()) }

            val bitmapImages = appBitmapStore.open(imagesUri)
            val bitmapsData: MutableList<Pair<Bitmap, BitmapDetails>> = mutableListOf()

            for(i in 0 until imagesUri.size) {
                val path = imagesUri[i].toString().substringAfterLast('/')
                bitmapsData.add(Pair(bitmapImages[i], BitmapDetails(path, "to-buy")))
                println(imagesUri[i].toString())
                println(path)
            }

            adapterData.addAll(bitmapsData)
            binding.bottom.recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private class Adapter(private val data: List<Any>) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val binding: PictureItemBinding = PictureItemBinding.bind(itemView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.picture_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val element = data[position] as Pair<Bitmap, BitmapDetails>
            val bitmap = element.first
            holder.binding.imageView.setImageBitmap(bitmap)
        }

        override fun getItemCount(): Int {
            return data.size
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

