package com.ifyezedev.coslog.feature.elements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.feature.elements.ElementsListAdapter.ElementsViewHolder
import java.lang.ClassCastException

/**
 * A base fragment that contains service dependencies for the element
 * list fragments contained within the view pager in [ElementsFragment]
 * */
abstract class ElementsListBaseFragment<T : ViewDataBinding> :
    BaseFragment<T>() {
    abstract override fun bindingLayoutId(): Int

    protected lateinit var cosplayController: NavController
    protected lateinit var elementsViewModel : ElementsViewModel

    override fun onAfterBindingCreated(view: View) {
        super.onAfterBindingCreated(view)
        val elementsFragment: ElementsFragment
        try {
            elementsFragment = parentFragment as ElementsFragment
        }
        catch(ex: ClassCastException) {
            throw ClassCastException("This class depends on ElementFragment to be its parent.")
        }

        cosplayController = elementsFragment.cosplayController
        elementsViewModel = elementsFragment.viewModel

        elementsViewModel.elements.observe(viewLifecycleOwner) { elementsData ->
            onElementsUpdated(elementsData)
        }
    }

    /**
     * This method provides the list of database elements when they are being updated.
     * */
    protected open fun onElementsUpdated(elements: List<Element>) {
    }
}

abstract class ElementsListAdapter<Binding: ViewDataBinding>(elements: List<Element>) :
    RecyclerView.Adapter<ElementsViewHolder<Binding>>() {

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