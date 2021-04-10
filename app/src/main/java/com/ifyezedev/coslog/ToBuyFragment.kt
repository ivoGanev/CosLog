package com.ifyezedev.coslog

import com.ifyezedev.coslog.databinding.FragmentToBuyBinding
import com.ifyezedev.coslog.feature.elements.ElementsBaseFragment

class ToBuyFragment : ElementsBaseFragment<FragmentToBuyBinding>() {

    override val galleryTag: String = "buy-gallery"

    override fun bindingLayoutId(): Int = R.layout.fragment_to_buy
}