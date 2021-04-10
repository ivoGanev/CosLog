package com.ifyezedev.coslog.feature.elements

import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.databinding.FragmentToMakeBinding

class ToMakeFragmentDetails : ElementsDetailsBaseFragment<FragmentToMakeBinding>() {
    override val galleryTag: String = "to-make-gallery"

    override fun bindingLayoutId(): Int = R.layout.fragment_to_make
}