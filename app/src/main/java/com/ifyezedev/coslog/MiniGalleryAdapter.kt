package com.ifyezedev.coslog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.core.data.BitmapHolder
import com.ifyezedev.coslog.core.etc.OnSnapPositionChangeListener
import com.ifyezedev.coslog.databinding.PictureItemBinding

class MiniGalleryAdapter(val data: MutableList<BitmapHolder>) :
    RecyclerView.Adapter<MiniGalleryAdapter.ViewHolderObject>() {

    private var _currentSelectedImagePosition: Int = RecyclerView.NO_POSITION
    val currentSelectedImagePosition: Int get() = _currentSelectedImagePosition

    val onSnapPositionChangeListener = OnSnapPositionChangeListener { position ->
        _currentSelectedImagePosition = position
    }

    lateinit var clickListener: OnClickListener

    fun addAll(data: List<BitmapHolder>) {
        this.data.addAll(data)
        this.notifyDataSetChanged()
    }

    /** @return true if the item has been successfully deleted. */
    fun removeItemAtCurrentSelectedPosition(): Boolean {
        if (currentSelectedImagePosition != RecyclerView.NO_POSITION) {
            data.removeAt(currentSelectedImagePosition)
            notifyDataSetChanged()
            return true
        }
        return false
    }

    /** @return null if there is an item selected */
    fun getCurrentSelectedItemFilePath(): String? {
        return data[currentSelectedImagePosition].filePath
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderObject {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.picture_item, parent, false)
        return ViewHolderObject(view, clickListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolderObject, position: Int) {
        val bitmapHolder = data[position] as BitmapHolder
        holder.binding.imageView.setImageBitmap(bitmapHolder.bitmap)
    }

    class ViewHolderObject(
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
