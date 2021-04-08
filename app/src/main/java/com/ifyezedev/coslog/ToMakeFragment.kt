package com.ifyezedev.coslog

import com.ifyezedev.coslog.databinding.FragmentToBuyBinding

class ToMakeFragment : ElementsBasePagerFragment<FragmentToBuyBinding>() {

    override val galleryTag: String = "to-make-gallery"

    override fun bindingLayoutId(): Int = R.layout.fragment_to_make
}