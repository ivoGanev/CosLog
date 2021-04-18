package com.ifyezedev.coslog

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.ifyezedev.coslog.core.common.BaseActivity
import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.di.activity.*
import com.ifyezedev.coslog.databinding.ActivityCosplayBinding
import javax.inject.Inject

class CosplayActivity : BaseActivity<ActivityCosplayBinding>() {
    override fun bindingLayoutId(): Int = R.layout.activity_cosplay

    private val cosplayActivityComponent: CosplayActivityComponent by lazy {
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cosplay_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.edit_cosplay -> {
                true
            }

            R.id.view_summary -> {
                //might be better to show a dialog here
                //using navigation global action, navigate to summary fragment
                //navController.navigate(R.id.action_global_summaryFragment)
                true
            }

            R.id.mark_completed -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    //let activity handle Up navigation
    override fun onSupportNavigateUp(): Boolean {
        return cosplayController.navigateUp()
    }
}