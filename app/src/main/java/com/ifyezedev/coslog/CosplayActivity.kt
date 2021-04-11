package com.ifyezedev.coslog

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.ifyezedev.coslog.core.common.BaseActivity
import com.ifyezedev.coslog.databinding.ActivityCosplayBinding

class CosplayActivity : BaseActivity<ActivityCosplayBinding>() {
    override fun bindingLayoutId(): Int = R.layout.activity_cosplay

    val cosplayCompositionRoot: CosplayActivityCompositionRoot by lazy {
        CosplayActivityCompositionRoot(this)
    }

    lateinit var cosplayController: NavController

    lateinit var appBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()

        binding.bottomNav.setupWithNavController(cosplayController)
        setupNavigationBackButton(appBar)
    }

    private fun inject() {
        cosplayController = cosplayCompositionRoot.cosplayController
        appBar = cosplayCompositionRoot.appBar
    }

    private fun setupNavigationBackButton(appBar: MaterialToolbar) {
        cosplayCompositionRoot.cosplayController.addOnDestinationChangedListener { controller, destination, _ ->
            if (destination.id == R.id.toBuyFragment
                || destination.id == R.id.toMakeFragment
                || destination.id == R.id.pictureViewerFragment
            ) {
                appBar.setNavigationOnClickListener {
                    controller.popBackStack()
                }
            } else {
                appBar.setNavigationOnClickListener {
                    controller.navigate(R.id.homeActivity)
                }
            }
        }
    }


}