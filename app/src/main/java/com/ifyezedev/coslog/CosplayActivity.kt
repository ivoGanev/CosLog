package com.ifyezedev.coslog

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.ifyezedev.coslog.core.common.BaseBindingActivity
import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.di.activity.*
import com.ifyezedev.coslog.core.di.app.AppComponent
import com.ifyezedev.coslog.core.di.app.AppModule
import com.ifyezedev.coslog.core.di.app.DaggerAppComponent
import com.ifyezedev.coslog.databinding.ActivityCosplayBinding
import javax.inject.Inject


class CosplayActivity : BaseBindingActivity<ActivityCosplayBinding>() {
    override fun bindingLayoutId(): Int = R.layout.activity_cosplay

    val cosplayActivityComponent: CosplayActivityComponent by lazy {
        DaggerCosplayActivityComponent.builder()
            .cosplayActivityModule(CosplayActivityModule(this))
            .appComponent((application as BaseApplication).appComponent)
            .build()
    }

    @Inject
    lateinit var cosplayController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cosplayActivityComponent.inject(this)

        binding.bottomNav.setupWithNavController(cosplayController)

        setSupportActionBar(binding.appToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cosplay_menu, menu)
        return true
    }
}