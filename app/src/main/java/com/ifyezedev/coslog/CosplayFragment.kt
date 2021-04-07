package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.databinding.FragmentCosplayMainBinding

/**
 * The [CosplayFragment] is used to render the main Cosplay flow.
 * */
class CosplayFragment : BaseFragment<FragmentCosplayMainBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_cosplay_main

    lateinit var appBar: MaterialToolbar
    lateinit var cosplayController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.cosplayNavHostFragment) as NavHostFragment
        cosplayController = navHostFragment.navController
        appBar = binding.appBarInclude.topAppBar

        binding {
            appBarInclude.topAppBar.setNavigationOnClickListener {
                cosplayController.navigate(R.id.homeActivity)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        appBar.setNavigationOnClickListener {
            cosplayController.popBackStack()
        }
    }
}