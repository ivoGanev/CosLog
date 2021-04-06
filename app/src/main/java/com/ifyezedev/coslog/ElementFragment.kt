package com.ifyezedev.coslog

import android.view.View
import com.ifyezedev.coslog.databinding.FragmentElementBinding


class ElementFragment : CosplayBaseFragment<FragmentElementBinding>(), View.OnClickListener {
    override fun bindingLayoutId() = R.layout.fragment_element

    private lateinit var tabLayoutSetup: ElementsTabLayoutSetup

    override fun onStart() {
        binding {
            fab.setOnClickListener(this@ElementFragment)

            tabLayoutSetup = ElementsTabLayoutSetup(
                requireContext(),
                elementsTabLayout,
                elementsViewPager,
                ElementsFragmentStateAdapter(this@ElementFragment),
                R.string.elements_tab_one_name, R.string.elements_tab_two_name
            )
        }
        super.onStart()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> onFabClicked()
        }
    }

    private fun onFabClicked() {
        when (binding.elementsTabLayout.selectedTabPosition) {
            0 -> dialogsController.navigate(R.id.toBuyFragment)
            1 -> dialogsController.navigate(R.id.toMakeFragment)
        }
    }
}


