package com.ifyezedev.coslog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.ifyezedev.coslog.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var drawerLayout: DrawerLayout
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initialize binding variable
        binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)

        //find NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.homeNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        //initialize drawerLayout variable
        drawerLayout = binding.drawerLayout

        //link navcontroller to ActionBar
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        //connect drawerLayout to navigation graph
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        //hook up the navigation UI up to the navigation view
        binding.navView.setupWithNavController(navController)






    }

    //let activity handle Up navigation
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.homeNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

}