package com.ifyezedev.coslog.feature.elements

import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.databinding.FragmentToMakeBinding

/**
 * This is the to-make details fragment which contains both the top and bottom parts of
 * the element details layout. Because the bottom part is the same for both
 * to-buy and to-make fragments we can reuse it. To get the bottom call [bottomBinding].
 * */
class ToMakeFragmentDetails : ElementsDetailsFragment<FragmentToMakeBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_to_make
}