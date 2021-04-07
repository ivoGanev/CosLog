package com.ifyezedev.coslog

import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.databinding.FragmentToBuyBinding
import com.ifyezedev.coslog.databinding.PictureItemBinding


class ToBuyFragment : CosplayBaseFragment<FragmentToBuyBinding>(), View.OnClickListener {

    override fun bindingLayoutId(): Int = R.layout.fragment_to_buy

    private val adapterData: MutableList<Bitmap> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottom.buttonAddImage.setOnClickListener(this)
        binding.bottom.recyclerView.adapter = Adapter(adapterData)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonAddImage -> onAddImage()
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
        if (resultCode == RESULT_OK && requestCode == 0) {
            // Handle single files
            val imagesUri: MutableList<Uri> = mutableListOf()
            intent?.let {
            }
            intent?.data?.let { uri -> imagesUri.add(uri) }
            intent?.clipData?.let { clipData -> imagesUri.addAll(clipData.mapUri()) }
            val bitmapImages = requireContext()
                .contentResolver
                .openInputStream(imagesUri)

            adapterData.addAll(bitmapImages)
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
            holder.binding.imageView.setImageBitmap(data[position] as Bitmap?)
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }

    private fun ContentResolver.openInputStream(uriList: List<Uri>): List<Bitmap> {
        val bitmapResult = mutableListOf<Bitmap>()
        for (i in uriList.indices) {
            val bitmapStream = this.openInputStream(uriList[i])
            val bitmap = BitmapFactory.decodeStream(bitmapStream)
            bitmapResult.add(bitmap)
        }
        return bitmapResult
    }

    private fun ClipData.mapUri(): MutableList<Uri> {
        val result = mutableListOf<Uri>()
        for (i in 0 until this.itemCount) {
            result.add(this.getItemAt(i).uri)
        }
        return result
    }
}

