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
        // The up navigation in CosplayActivity has two separate navigation implementations:
        // 1) Navigate to homeActivity
        // 2) Navigate up

        // Why two separate implementations?
        //
        // Because by default super.onSupportNavigateUp() will navigate to a previous intent
        // using Intent upIntent = getSupportParentActivityIntent(); which has nothing to do
        // with the nav graphs and controllers: it's just the old Android way of going back, but
        // when we override onSupportNavigateUp() to use controller.navigateUp(), we can't go back
        // to the previous activity (HomeActivity) because the controller manages only its own
        // navigation graph (cosplay_nav_graph in our case). You can get the hint from this
        // answer: https://stackoverflow.com/questions/50452359/navigation-architecture-component-activities,
        // by one of the Navigation creators(Ian Lake).
        //
        // "The navigation graph only exists within a single activity. As per the Migrate to Navigation guide,
        // <activity> destinations can be used to start an Activity from within the navigation graph,
        // but once that second activity is started, it is totally separate from the original
        // navigation graph (it could have its own graph or just be a simple activity)."
        //
        // Now the question is how to solve it.
        // My approach is to make each fragment related to this CosplayActivity to inherit from
        // a base fragment class which would override the back button
        //
        // BaseCosplayActivityFragment {
        // {
        // open fun onOptionsItemSelected(item: MenuItem) {
        //      cosplayController.navigate(R.id.homeActivity)
        // }
        //
        // and the fragments which need to navigate back can override it separately
        // PictureGalleryFragment: BaseCosplayActivityFragment {
        // {
        // override fun onOptionsItemSelected(item: MenuItem) {
        //      cosplayController.navigate(R.id.homeActivity)
        // }
        // }

        return cosplayController.navigateUp()
    }
}