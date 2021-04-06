package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.ui.setupWithNavController
import com.ifyezedev.coslog.core.common.BaseDialogFragment
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.databinding.FragmentCosplayMainBinding


class CosplayFragment : CosplayBaseFragment<FragmentCosplayMainBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_cosplay_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding {
            appBarInclude.topAppBar.setNavigationOnClickListener {
                compositionRoot.cosplayController.navigate(R.id.homeActivity)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.bottomNav.setupWithNavController(compositionRoot.cosplayController)
    }
}