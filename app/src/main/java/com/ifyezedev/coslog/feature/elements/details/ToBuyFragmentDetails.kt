package com.ifyezedev.coslog.feature.elements.details

import android.os.Bundle
import android.view.View
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.data.db.entities.elementsBuilder
import com.ifyezedev.coslog.databinding.FragmentToBuyBinding
import com.ifyezedev.coslog.feature.elements.details.ElementsDetailsFragment

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
        super.onSaveButtonPressed()
        detailsViewModel.insertElement(elementsBuilder {
            name = binding.nameValue.text.toString()
            //cost = binding.costValue.text.toString().toDouble()
        })
    }
}