package com.ifyezedev.coslog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.ifyezedev.coslog.databinding.FragmentElementBinding
import com.ifyezedev.coslog.databinding.FragmentElementToBuyBinding
import com.ifyezedev.coslog.databinding.FragmentElementToMakeBinding
import java.lang.IllegalArgumentException

class ElementFragment : Fragment() {
    private lateinit var elementsFragmentStateAdapter: ElementsFragmentStateAdapter
    private lateinit var binding: FragmentElementBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentElementBinding.inflate(inflater)
        return binding.root
    }

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

class ElementsToBuyFragment : Fragment() {
    private lateinit var binding: FragmentElementToBuyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentElementToBuyBinding.inflate(inflater)
        return binding.root
    }
}

class ElementsToMakeFragment : Fragment() {
    private lateinit var binding: FragmentElementToMakeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentElementToMakeBinding.inflate(inflater)
        return binding.root
    }
}