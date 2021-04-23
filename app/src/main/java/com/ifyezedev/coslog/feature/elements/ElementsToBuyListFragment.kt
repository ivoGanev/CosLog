package com.ifyezedev.coslog.feature.elements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.databinding.ElementItemToBuyBinding
import com.ifyezedev.coslog.databinding.FragmentElementToBuyListBinding
import com.ifyezedev.coslog.feature.elements.details.ElementsDetailsFragment

class ElementsToBuyListFragment : ElementsListBaseFragment<FragmentElementToBuyListBinding>(),
    ElementsListAdapter.OnClickListener {
    override fun bindingLayoutId(): Int = R.layout.fragment_element_to_buy_list
    private lateinit var adapter: Adapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = Adapter(listOf())
        adapter.clickListener = this
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onElementsUpdated(elements: List<Element>) {
        adapter.setElements(elements)
    }

    fun navigateToBuyDetailsFragmentForNewItem() {
        cosplayController.navigate(R.id.toBuyFragment,
            ElementsDetailsFragment.getNewItemBundle(null))
    }

    override fun onEntireElementClickedListener(position: Int) {
        cosplayController.navigate(R.id.toBuyFragment,
            ElementsDetailsFragment.getNewItemBundle(adapter.elements[position]))
    }

    private class Adapter(
        data: List<Element>,
        override val layoutId: Int = R.layout.element_item_to_buy
    ) :
        ElementsListAdapter<ElementItemToBuyBinding>(data) {

        override fun onBindViewHolder(
            holder: ElementsViewHolder<ElementItemToBuyBinding>,
            position: Int,
        ) = with(holder.binding) {
            name.text = elements[position].name
            cost.text = elements[position].cost.toString()
        }
    }

}
