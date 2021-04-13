package com.ifyezedev.coslog

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.ifyezedev.coslog.core.common.BaseActivity
import com.ifyezedev.coslog.databinding.ActivityCosplayBinding


class CosplayActivity : BaseActivity<ActivityCosplayBinding>() {
    override fun bindingLayoutId(): Int = R.layout.activity_cosplay

    val cosplayCompositionRoot: CosplayActivityCompositionRoot by lazy {
        CosplayActivityCompositionRoot(this)
    }

    lateinit var cosplayController: NavController

    lateinit var appBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cosplayController = cosplayCompositionRoot.cosplayController

        appBar = cosplayCompositionRoot.appBar

        binding.bottomNav.setupWithNavController(cosplayController)

        setSupportActionBar(appBar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cosplay_menu, menu)
        return true
    }
}