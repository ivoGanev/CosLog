package com.ifyezedev.coslog.feature.elements

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ifyezedev.coslog.CosplayBaseFragment
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.databinding.FragmentElementBinding


class ElementFragment : CosplayBaseFragment<FragmentElementBinding>(), View.OnClickListener {
    override fun bindingLayoutId() = R.layout.fragment_element

    override fun onStart() {
        super.onStart()
        binding {
            fab.setOnClickListener(this@ElementFragment)

            initTabLayout(
                requireContext(),
                elementsTabLayout,
                elementsViewPager,
                ElementsFragmentStateAdapter(this@ElementFragment),
                R.string.elements_tab_one_name, R.string.elements_tab_two_name
            )
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> onFabClicked()
        }
    }

    private fun onFabClicked() {
        when (binding.elementsTabLayout.selectedTabPosition) {
            0 -> cosplayController.navigate(R.id.toBuyFragment)
            1 -> cosplayController.navigate(R.id.toMakeFragment)
        }
    }

    private fun initTabLayout(
        context: Context,
        tabLayout: TabLayout,
        viewPager2: ViewPager2,
        viewPagerAdapter: FragmentStateAdapter,
        @StringRes vararg titles: Int
    ) {
        viewPager2.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> context.getString(titles[0])
                1 -> context.getString(titles[1])
                else -> throw IllegalArgumentException("Error. Shouldn't be on this fragment page.")
            }
        }.attach()
    }
}


