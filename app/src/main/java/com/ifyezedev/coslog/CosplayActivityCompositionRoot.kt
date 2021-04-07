package com.ifyezedev.coslog

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.MaterialToolbar
import com.ifyezedev.coslog.databinding.ActivityCosplayBinding

class CosplayActivityCompositionRoot() {
    lateinit var cosplayController: NavController

    lateinit var cosplayAppBar: MaterialToolbar

    fun createCosplayController(fragment: CosplayFragment) {
        cosplayController = fragment.cosplayController
    }

    fun createCosplayAppBar(fragment: CosplayFragment) {
        cosplayAppBar = fragment.appBar
    }
}