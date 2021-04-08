package com.ifyezedev.coslog

import com.ifyezedev.coslog.databinding.FragmentToBuyBinding

class ToBuyFragment : ElementsBasePagerFragment<FragmentToBuyBinding>() {

    override val galleryTag: String = "buy-gallery"

    override fun bindingLayoutId(): Int = R.layout.fragment_to_buy
}