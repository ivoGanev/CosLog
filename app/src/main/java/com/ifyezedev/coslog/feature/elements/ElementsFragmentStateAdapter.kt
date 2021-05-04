package com.ifyezedev.coslog.feature.elements

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.IllegalArgumentException

private const val PAGES : Int = 2

class ElementsFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = PAGES

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ElementsToBuyFragment()
        1 -> ElementsToMakeFragment()
        else -> throw IllegalArgumentException("Unsupported fragment page")
    }
}