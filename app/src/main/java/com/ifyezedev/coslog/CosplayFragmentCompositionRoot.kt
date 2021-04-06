package com.ifyezedev.coslog

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.ifyezedev.coslog.CosplayActivity

class CosplayFragmentCompositionRoot(private val activity: FragmentActivity) {
    val cosplayController: NavController get() = (activity as CosplayActivity).cosplayCompositionRoot.cosplayController
}