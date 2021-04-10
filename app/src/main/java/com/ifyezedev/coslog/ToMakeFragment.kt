package com.ifyezedev.coslog

import com.ifyezedev.coslog.databinding.FragmentToBuyBinding
import com.ifyezedev.coslog.feature.elements.ElementsBaseFragment

class ToMakeFragment : ElementsBaseFragment<FragmentToBuyBinding>() {

    override val galleryTag: String = "to-make-gallery"

    override fun bindingLayoutId(): Int = R.layout.fragment_to_make
}