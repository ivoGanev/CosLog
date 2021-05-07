package com.ifyezedev.coslog.feature.elements

import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.databinding.ElementItemToBuyBinding

 class ElementsToBuyAdapter(
    data: List<Element>,
    override val layoutId: Int = R.layout.element_item_to_buy,
) :
    ElementsAdapter<ElementItemToBuyBinding>(data) {

    override fun onBindViewHolder(
        holder: ElementsViewHolder<ElementItemToBuyBinding>,
        position: Int,
    ) = with(holder.binding) {
        name.text = elements[position].name
        cost.text = elements[position].cost.toString()
    }
}