package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.ifyezedev.coslog.databinding.FragmentElementBinding
import java.lang.IllegalArgumentException


class ElementFragment : BindingFragment<FragmentElementBinding>() {
    private lateinit var elementsFragmentStateAdapter: ElementsFragmentStateAdapter
    override fun bindingLayoutId() = R.layout.fragment_element

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            elementsFragmentStateAdapter = ElementsFragmentStateAdapter(this@ElementFragment)
            elementsViewPager.adapter = elementsFragmentStateAdapter
            TabLayoutMediator(elementsTabLayout, elementsViewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "To Buy"
                    1 -> "To Make"
                    else -> throw IllegalArgumentException("Unsupported fragment page")
                }
            }.attach()
        }
    }
}


