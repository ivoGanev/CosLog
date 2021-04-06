package com.ifyezedev.coslog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import androidx.navigation.NavController

class CosplayActivity : AppCompatActivity() {
    val cosplayCompositionRoot: CosplayActivityCompositionRoot = CosplayActivityCompositionRoot(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cosplay)
        supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentLifecycleCallbacks() {

            override fun onFragmentViewCreated(
                fm: FragmentManager,
                f: androidx.fragment.app.Fragment,
                v: View,
                savedInstanceState: Bundle?
            ) {
                if (f is CosplayFragment) {
                    cosplayCompositionRoot.createCosplayController(f)
                }
                super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            }
        }, true)
    }
}