package com.ifyezedev.coslog

import android.content.Context
import androidx.annotation.StringRes
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Attaches the TabLayout to the ViewPager2 and ensures setting an adapter to the ViewPager2.
 * */
class ElementsTabLayoutSetup(
    context: Context,
    tabLayout: TabLayout,
    viewPager2: ViewPager2,
    viewPagerAdapter: FragmentStateAdapter,
    @StringRes vararg titles: Int
) {
    init {
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