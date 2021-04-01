package com.ifyezedev.coslog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.databinding.ElementItemToBuyBinding
import com.ifyezedev.coslog.databinding.FragmentElementPictureViewerBinding

class ElementsFragmentPictureViewer : BindingFragment<FragmentElementPictureViewerBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_element_picture_viewer
    private lateinit var recyclerView: RecyclerView

    override fun onBindingCreated(){
        recyclerView = binding.imageRecyclerView
        recyclerView.adapter = Adapter(listOf(1, 2, 3, 4))
    }

    private class Adapter(private val data: List<Any>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView : ImageView = itemView.findViewById(R.id.imageView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.picture_viewer_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.imageView.setImageResource(R.drawable.ic_money)
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }
}