package com.ifyezedev.coslog.feature.elements

import android.os.Bundle
import android.view.View
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.databinding.FragmentToBuyBinding

class ToBuyFragmentDetails : ElementsDetailsFragment<FragmentToBuyBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_to_buy

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottomBinding.recyclerView
        super.onViewCreated(view, savedInstanceState)
    }
}