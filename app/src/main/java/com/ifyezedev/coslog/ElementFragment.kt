package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.ifyezedev.coslog.databinding.FragmentElementBinding
import java.lang.IllegalArgumentException


class ElementFragment : BindingFragment<FragmentElementBinding>(), View.OnClickListener {
    override fun bindingLayoutId() = R.layout.fragment_element

    private lateinit var elementsFragmentStateAdapter: ElementsFragmentStateAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            binding.fab.setOnClickListener(this@ElementFragment)
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> onFabClicked()
        }
    }

    private fun onFabClicked() {
        val navHostFragment = requireActivity()
            .supportFragmentManager
            .findFragmentById(R.id.cosplayNavHostFragment) as NavHostFragment

        navHostFragment.navController.navigate(R.id.toBuyFragment)
    }
}


