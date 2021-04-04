package com.ifyezedev.coslog

import android.content.Context
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ElementsTabLayoutSetup(
    context: Context,
    tabLayout: TabLayout,
    viewPager2: ViewPager2,
    @StringRes vararg titles: Int
) {
    init {
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> context.getString(titles[0])
                1 -> context.getString(titles[1])
                else -> throw IllegalArgumentException("Error. Shouldn't be on this fragment page.")
            }
        }.attach()
    }
}