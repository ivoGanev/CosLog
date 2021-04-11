package com.ifyezedev.coslog.feature.elements

import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.databinding.FragmentToBuyBinding

class ToBuyFragmentDetails : ElementsDetailsBaseFragment<FragmentToBuyBinding>() {
    override val galleryTag: String = "buy-gallery"

    override fun bindingLayoutId(): Int = R.layout.fragment_to_buy
}