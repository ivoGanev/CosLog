package com.ifyezedev.coslog.feature.elements

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.databinding.ElementItemToBuyBinding
import com.ifyezedev.coslog.databinding.FragmentElementToBuyListBinding
import com.ifyezedev.coslog.feature.elements.details.ElementsDetailsFragment
import com.ifyezedev.coslog.feature.elements.details.ToBuyFragmentDetailsDirections

class ElementsToBuyFragment : ElementsListBaseFragment<FragmentElementToBuyListBinding>(),
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
        // Will display only elements that are only to be bought.
        adapter.setElements(elements.filter { element -> element.isBuy })
    }

    fun navigateToBuyDetailsFragmentForNewItem() {
        cosplayController.navigate(R.id.toBuyFragmentDetails,
            ElementsDetailsFragment.getNewItemBundle(null))
    }

    override fun onEntireElementClickedListener(position: Int) {
        cosplayController.navigate(R.id.toBuyFragmentDetails,
            ElementsDetailsFragment.getNewItemBundle(adapter.elements[position]))
    }

    private class Adapter(
        data: List<Element>,
        override val layoutId: Int = R.layout.element_item_to_buy,
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
