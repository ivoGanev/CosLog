package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ifyezedev.coslog.databinding.FragmentElementBinding
import java.lang.IllegalArgumentException


class ElementFragment : CosplayGraphBaseFragment<FragmentElementBinding>(), View.OnClickListener {
    override fun bindingLayoutId() = R.layout.fragment_element

    private lateinit var elementsFragmentStateAdapter: ElementsFragmentStateAdapter

    private lateinit var tabLayout: TabLayout

    override fun onStart() {
        with(binding) {
            tabLayout = elementsTabLayout
            fab.setOnClickListener(this@ElementFragment)
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
        super.onStart()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> onFabClicked()
        }
    }

    private fun onFabClicked() {
        when (tabLayout.selectedTabPosition) {
            0 -> cosplayController.navigate(R.id.toBuyFragment)
            1 -> cosplayController.navigate(R.id.toMakeFragment)
        }
    }
}


