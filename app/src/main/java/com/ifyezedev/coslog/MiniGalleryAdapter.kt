package com.ifyezedev.coslog

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.databinding.PictureItemBinding

class MiniGalleryAdapter(private val data: MutableList<Bitmap>) :
    RecyclerView.Adapter<MiniGalleryAdapter.ViewHolder>() {

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
