package com.ifyezedev.coslog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.core.data.BitmapHolder
import com.ifyezedev.coslog.databinding.PictureItemBinding

class MiniGalleryAdapter(val data: MutableList<BitmapHolder>) :
    RecyclerView.Adapter<MiniGalleryAdapter.ViewHolder>() {

    lateinit var clickListener: OnClickListener

    fun addAll(data: List<BitmapHolder>) {
        this.data.addAll(data)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.picture_item, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bitmapHolder = data[position] as BitmapHolder
        holder.binding.imageView.setImageBitmap(bitmapHolder.bitmap)
    }

    class ViewHolder(
        itemView: View,
        private val clickListener: OnClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val binding: PictureItemBinding = PictureItemBinding.bind(itemView)

        init {
            binding.imageView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                binding.imageView.id -> clickListener.onImageClickedListener(binding.imageView)
            }
        }
    }

    interface OnClickListener {
        fun onImageClickedListener(view: View)
    }
}
