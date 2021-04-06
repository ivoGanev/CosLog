package com.ifyezedev.coslog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.ifyezedev.coslog.databinding.FragmentElementBinding
import com.ifyezedev.coslog.databinding.FragmentElementToBuyBinding
import com.ifyezedev.coslog.databinding.FragmentElementToMakeBinding
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

class ElementsFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> ElementsToBuyFragment()
            1 -> ElementsToMakeFragment()
            else -> throw IllegalArgumentException("Unsupported fragment page")
        }
        return fragment
    }
}

class ElementsToBuyFragment : BindingFragment<FragmentElementToBuyBinding>() {
    override fun bindingLayoutId(): Int  = R.layout.fragment_element_to_buy
}

class ElementsToMakeFragment : BindingFragment<FragmentElementToMakeBinding>() {
    override fun bindingLayoutId(): Int  = R.layout.fragment_element_to_make
}