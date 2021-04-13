package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.ifyezedev.coslog.core.common.BaseActivity
import com.ifyezedev.coslog.databinding.ActivityCosplayBinding


class CosplayActivity : BaseActivity<ActivityCosplayBinding>(), View.OnClickListener {
    override fun bindingLayoutId(): Int = R.layout.activity_cosplay

    val cosplayCompositionRoot: CosplayActivityCompositionRoot by lazy {
        CosplayActivityCompositionRoot(this)
    }

    lateinit var cosplayController: NavController

    lateinit var appBar: MaterialToolbar

    lateinit var cosplayToolbarController: CosplayToolbarController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cosplayController = cosplayCompositionRoot.cosplayController
        appBar = cosplayCompositionRoot.appBar
        cosplayToolbarController = cosplayCompositionRoot.cosplayToolbarController

        binding.bottomNav.setupWithNavController(cosplayController)
    }


    override fun onClick(v: View?) {
        cosplayToolbarController.displayAppBar(CosplayToolbarController.ToolbarType.PictureGallery)
    }
}