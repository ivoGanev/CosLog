package com.ifyezedev.coslog.feature.elements

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.databinding.FragmentElementToMakeListBinding
import com.ifyezedev.coslog.feature.elements.details.ElementsDetailsFragment

class ElementsToMakeFragment : ElementsBaseFragment<FragmentElementToMakeListBinding>(), ElementsAdapter.OnClickListener {
    override fun bindingLayoutId(): Int = R.layout.fragment_element_to_make_list
    private lateinit var adapter: ElementsToMakeAdapter

    private val nextNavigationDestination = R.id.toMakeGraph

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ElementsToMakeAdapter(listOf())
        adapter.clickListener = this

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onElementsUpdated(elements: List<Element>) {
        // Will display only elements that are only to be made.
        adapter.setElements(elements.filter { element -> !element.isBuy })
    }

    fun navigateToMakeDetailsFragmentForNewItem() {
        cosplayController.navigate(nextNavigationDestination,
            ElementsDetailsFragment.getNewItemBundle(null))
    }

    override fun onEntireElementClickedListener(position: Int) {
        cosplayController.navigate(nextNavigationDestination,
            ElementsDetailsFragment.getNewItemBundle(adapter.elements[position]))
    }
}