package com.ifyezedev.coslog.feature.elements

import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ifyezedev.coslog.CosplayActivityBaseFragment
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.data.db.CosLogDatabase
import com.ifyezedev.coslog.databinding.FragmentElementBinding


class ElementFragment : CosplayActivityBaseFragment<FragmentElementBinding>(),
    View.OnClickListener {
    override fun bindingLayoutId() = R.layout.fragment_element

    private lateinit var adapter: ElementsFragmentStateAdapter

    private lateinit var viewModel: ElementsViewModel

    override fun onStart() {
        super.onStart()

        val viewModelFactory =
            ElementsViewModel.ElementsViewModelFactory(CosLogDatabase.getDatabase(requireContext()).cosLogDao)
        viewModel =
            ViewModelProvider(viewModelStore, viewModelFactory).get(ElementsViewModel::class.java)

        adapter = ElementsFragmentStateAdapter(this)
        binding {
            fab.setOnClickListener(this@ElementFragment)
            initTabLayout(
                requireContext(),
                elementsTabLayout,
                elementsViewPager,
                adapter,
                R.string.elements_tab_one_name, R.string.elements_tab_two_name
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            cosplayController.navigate(R.id.homeActivity)
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> onFabClicked()
        }
    }

    private fun onFabClicked() {
        val fragment =
            childFragmentManager.findFragmentByTag("f" + binding.elementsViewPager.currentItem)

        when (fragment) {
            is ElementsToBuyListFragment -> fragment.navigateToBuyDetailsFragmentForNewItem()
            is ElementsToMakeListFragment -> fragment.navigateToMakeDetailsFragment()
        }
    }

    private fun initTabLayout(
        context: Context,
        tabLayout: TabLayout,
        viewPager2: ViewPager2,
        viewPagerAdapter: FragmentStateAdapter,
        @StringRes vararg titles: Int,
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


