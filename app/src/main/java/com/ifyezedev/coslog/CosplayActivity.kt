package com.ifyezedev.coslog

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class CosplayActivity : AppCompatActivity() {
    private val cosplayCompositionRoot: CosplayCompositionRoot = CosplayCompositionRoot(this)

    lateinit var cosplayController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cosplay)

        if(savedInstanceState==null) {
            cosplayController = cosplayCompositionRoot.cosplayController
        }
        println("Cosplay Activity")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.cosplay_menu, menu)
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
//                navController.navigate(R.id.action_global_summaryFragment)
                true
            }

            R.id.mark_completed -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}