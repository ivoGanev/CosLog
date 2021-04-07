package com.ifyezedev.coslog

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI

class CosplayFragmentCompositionRoot(
    private val activity: CosplayActivity) {

    private val activityCompositionRoot: CosplayActivityCompositionRoot = activity.cosplayCompositionRoot

    val cosplayController = activityCompositionRoot.cosplayController

    val appBar = activityCompositionRoot.cosplayAppBar
}