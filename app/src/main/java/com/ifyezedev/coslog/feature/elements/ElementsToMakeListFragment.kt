package com.ifyezedev.coslog.feature.elements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.CosplayActivity
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.databinding.ElementItemToBuyBinding
import com.ifyezedev.coslog.databinding.ElementItemToMakeBinding
import com.ifyezedev.coslog.databinding.FragmentElementToMakeListBinding
import com.ifyezedev.coslog.feature.elements.ElementsAdapter.*

class ElementsToMakeListFragment : BaseFragment<FragmentElementToMakeListBinding>(), OnClickListener {
    override fun bindingLayoutId(): Int = R.layout.fragment_element_to_make_list
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ElementsAdapter
    private lateinit var cosplayController : NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cosplayController = (requireActivity() as CosplayActivity).cosplayController

        adapter = ElementsAdapter(listOf(Element(1, 2, "To Make", true, 2.0, 2, "S", "Notes")))
        adapter.clickListener = this

        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    fun navigateToMakeDetailsFragment() {
        cosplayController.navigate(R.id.toMakeFragment,
            ElementsDetailsFragment.getNewItemBundle(null))
    }

    override fun onEntireElementClickedListener(position: Int) {
        cosplayController.navigate(R.id.toMakeFragment,
            ElementsDetailsFragment.getNewItemBundle(adapter.data[position]))
    }
}

private class ElementsAdapter(val data: List<Element>) :
    RecyclerView.Adapter<ViewHolder>() {
    var clickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.element_item_to_make, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.durationTextView.text = data[position].name
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View, private val clickListener: OnClickListener?,
    ) : RecyclerView.ViewHolder(itemView) , View.OnClickListener {
        val binding: ElementItemToMakeBinding = ElementItemToMakeBinding.bind(itemView)

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
