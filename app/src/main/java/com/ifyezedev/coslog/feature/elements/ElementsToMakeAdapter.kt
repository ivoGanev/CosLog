package com.ifyezedev.coslog.feature.elements

import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.databinding.ElementItemToMakeBinding

class ElementsToMakeAdapter(
    data: List<Element>,
    override val layoutId: Int = R.layout.element_item_to_make,
) :
    ElementsAdapter<ElementItemToMakeBinding>(data) {

    override fun onBindViewHolder(
        holder: ElementsViewHolder<ElementItemToMakeBinding>,
        position: Int,
    ) = with(holder.binding) {
        name.text = elements[position].name
        time.text = elements[position].progress
    }
}