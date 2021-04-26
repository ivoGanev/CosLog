package com.ifyezedev.coslog.feature.elements.details

import android.os.Bundle
import android.view.View
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.data.db.entities.elementsBuilder
import com.ifyezedev.coslog.databinding.FragmentToBuyBinding

/**
 * This is the to-buy elements fragment which contains both the top and bottom parts of
 * the element details layout. Because the bottom part is the same for both
 * to-buy and to-make fragments we can reuse it. To get the bottom call [bottomBinding].
 * */
class ToBuyFragmentDetails : ElementsDetailsFragment<FragmentToBuyBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_to_buy

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottomBinding.recyclerView
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSaveButtonPressed() {
        val elementTmp = elementsBuilder {
            if(element!=null)
                eid = element!!.eid

            name = binding.nameValue.text.toString()
            source = binding.sourceValue.text.toString()
            cost = binding.costValue.text.toString().toDouble()
            notes = binding.bottomView.notes.text.toString()
            images = ArrayList(adapter.getFilePaths())
            isBuy = true
        }

        if (element == null)
            detailsViewModel.insertElementInDatabase(elementTmp)
        else {
            detailsViewModel.updateElementInDatabase(elementTmp)
        }
    }

    // This method will get executed from the base fragment [ElementsDetailsFragment] if we
    // are updating the fragment - when we click over an element from the elements list.
    override fun initializeWithElement(element: Element) {
        super.initializeWithElement(element)
        binding.nameValue.setText(element.name)
        binding.sourceValue.setText(element.source)
        binding.costValue.setText(element.cost.toString())
        binding.bottomView.notes.setText(element.notes)
    }
}