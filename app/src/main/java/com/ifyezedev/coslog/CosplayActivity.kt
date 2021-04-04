package com.ifyezedev.coslog

import android.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class CosplayActivity : AppCompatActivity() {
    private val cosplayCompositionRoot: CosplayCompositionRoot = CosplayCompositionRoot(this)

    lateinit var cosplayNavController: NavController
    lateinit var dialogsController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cosplay)

        if (savedInstanceState == null) {
            cosplayNavController = cosplayCompositionRoot.cosplayController
            println(cosplayNavController.graph)

            supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                FragmentLifecycleCallbacks() {

                override fun onFragmentViewCreated(
                    fm: FragmentManager,
                    f: androidx.fragment.app.Fragment,
                    v: View,
                    savedInstanceState: Bundle?
                ) {
                    if (f is CosplayFragment) {
                        dialogsController = cosplayCompositionRoot.dialogsController(f)
                    }
                    super.onFragmentViewCreated(fm, f, v, savedInstanceState)
                }
            }, true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.cosplay_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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