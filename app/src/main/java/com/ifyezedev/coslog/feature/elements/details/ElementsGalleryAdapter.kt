package com.ifyezedev.coslog.feature.elements.details

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.core.etc.OnSnapPositionChangeListener
import com.ifyezedev.coslog.databinding.PictureItemBinding

class ElementsGalleryAdapter(data: List<Pair<String, Bitmap>> = listOf()) :

    RecyclerView.Adapter<ElementsGalleryAdapter.ViewHolderObject>() {

    private val _data: MutableList<Pair<String,Bitmap>> = data.toMutableList()
    val data: List<Pair<String,Bitmap>> get() = _data

    private var _currentSelectedImagePosition: Int = RecyclerView.NO_POSITION
    val currentSelectedImagePosition: Int get() = _currentSelectedImagePosition

    val onSnapPositionChangeListener = OnSnapPositionChangeListener { position ->
        _currentSelectedImagePosition = position
    }

    lateinit var clickListener: OnClickListener

    fun addAll(paths: List<Pair<String, Bitmap>>) {
        // inserting in a queue fashioned way will
        // ensure that the inserted image will be
        // the one that is always visible
        _data.addAll(0, paths)
        notifyDataSetChanged()
    }

    fun setData(paths: List<Pair<String, Bitmap>>) {
        _data.clear()
        addAll(paths)
    }

    override fun getItemId(position: Int): Long {
        return _data[position].hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderObject {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.picture_item, parent, false)
        return ViewHolderObject(view, clickListener)
    }

    override fun getItemCount(): Int {
        return _data.size
    }

    override fun onBindViewHolder(holder: ViewHolderObject, position: Int) {
        holder.binding.imageView.setImageBitmap(_data[position].second)
    }

    fun  getFilePaths(): List<String> = _data
        .map { it.first }

    class ViewHolderObject(
        itemView: View,
        private val clickListener: OnClickListener,
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val binding: PictureItemBinding = PictureItemBinding.bind(itemView)

        init {
            binding.imageView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                binding.imageView.id -> clickListener.onPictureClicked(binding.imageView)
            }
        }
    }

    interface OnClickListener {
        fun onPictureClicked(view: View)
    }
}
