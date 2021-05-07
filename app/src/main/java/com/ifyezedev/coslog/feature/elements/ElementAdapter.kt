package com.ifyezedev.coslog.feature.elements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.data.db.entities.Element


/**
 * Reusable behavior for recycler view that stores a list of elements.
 * 'To Buy' and 'To Make' recycler views share a lot of common logic but have
 * slightly different views to display for the user, hence this class is created to
 * inherit the common functionality.
 * */
abstract class ElementsAdapter<Binding: ViewDataBinding>(elements: List<Element>) :
    RecyclerView.Adapter<ElementsAdapter.ElementsViewHolder<Binding>>() {

    val elements : List<Element> get() = _elements
    private val _elements = elements.toMutableList()

    abstract val layoutId: Int

    var clickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementsViewHolder<Binding> {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return ElementsViewHolder(view, clickListener)
    }

    abstract override fun onBindViewHolder(holder: ElementsViewHolder<Binding>, position: Int)

    override fun getItemCount(): Int = _elements.size

    fun setElements(elements: List<Element>) {
        _elements.clear()
        _elements.addAll(elements)
        notifyDataSetChanged()
    }

    /**
     * Holds the binding and listens for when the root view is clicked.
     * */
    class ElementsViewHolder<Binding : ViewDataBinding>(
        itemView: View,
        private val clickListener: OnClickListener?,
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val binding: Binding = DataBindingUtil.bind(itemView)!!

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener?.onEntireElementClickedListener(adapterPosition)
        }
    }

    interface OnClickListener {
        fun onEntireElementClickedListener(position: Int)
    }
}