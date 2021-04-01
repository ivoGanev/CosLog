package com.ifyezedev.coslog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class CosplayActivity : AppCompatActivity() {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cosplay)

        //enable the Up button
       // supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //find the NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.cosplay_activity_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onStart() {
        super.onStart()
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.cosplay_menu, menu)
//        return true
//    }
//
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when(item.itemId) {
//            R.id.edit_cosplay -> {
//                true
//            }
//
//            R.id.view_summary -> {
//                //might be better to show a dialog here
//                //using navigation global action, navigate to summary fragment
////                navController.navigate(R.id.action_global_summaryFragment)
//                true
//            }
//
//            R.id.mark_completed -> {
//                true
//            }
//
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}