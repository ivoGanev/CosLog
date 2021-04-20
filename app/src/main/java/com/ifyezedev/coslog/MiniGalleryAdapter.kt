package com.ifyezedev.coslog

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromAndroidGallery
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.etc.OnSnapPositionChangeListener
import com.ifyezedev.coslog.databinding.PictureItemBinding
import kotlinx.coroutines.CoroutineScope

class MiniGalleryAdapter() :

    RecyclerView.Adapter<MiniGalleryAdapter.ViewHolderObject>() {
    private val bitmaps = mutableListOf<Pair<String, Bitmap>>()

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
        bitmaps.addAll(0, paths)
        notifyDataSetChanged()
    }

    fun getData() = bitmaps.toList()

    fun clear() {
        bitmaps.clear()
    }

    override fun getItemId(position: Int): Long {
        return bitmaps[position].hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderObject {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.picture_item, parent, false)
        return ViewHolderObject(view, clickListener)
    }

    override fun getItemCount(): Int {
        return bitmaps.size
    }

    override fun onBindViewHolder(holder: ViewHolderObject, position: Int) {
        holder.binding.imageView.setImageBitmap(bitmaps[position].second)
    }

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
                binding.imageView.id -> clickListener.onImageClickedListener(binding.imageView)
            }
        }
    }

    interface OnClickListener {
        fun onImageClickedListener(view: View)
    }
}
