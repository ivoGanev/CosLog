package com.ifyezedev.coslog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import androidx.navigation.NavController
import com.google.android.material.appbar.MaterialToolbar
import com.ifyezedev.coslog.core.common.BaseActivity
import com.ifyezedev.coslog.core.common.StandardBindingAgent
import com.ifyezedev.coslog.databinding.ActivityCosplayBinding

class CosplayActivity : BaseActivity<ActivityCosplayBinding>() {
    override fun bindingLayoutId(): Int = R.layout.activity_cosplay

    val cosplayCompositionRoot = CosplayActivityCompositionRoot()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cosplay)

        val fragment = CosplayFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

        if (savedInstanceState == null)
            injectDependencies()
    }

    private fun setupNavigationBackButton(appBar: MaterialToolbar) {
        cosplayCompositionRoot.cosplayController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.toBuyFragment || destination.id == R.id.toMakeFragment) {
                appBar.setNavigationOnClickListener {
                    controller.popBackStack()
                }
            } else {
                appBar.setNavigationOnClickListener {
                    controller.navigate(R.id.homeActivity)
                }
            }
            println(destination)
        }
    }

    private fun injectDependencies() {
        /*
        * Creating the CosplayFragment dependencies is not straight forward.
        * In order to grab its dependencies we have to wait for the fragment
        * view to be created.
        * */
        supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(
                fm: FragmentManager,
                f: Fragment,
                v: View,
                savedInstanceState: Bundle?
            ) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState)
                if (f is CosplayFragment) {
                    cosplayCompositionRoot.createCosplayController(f)
                    cosplayCompositionRoot.createCosplayAppBar(f)
                    setupNavigationBackButton(cosplayCompositionRoot.cosplayAppBar)
                }
            }
        }, true)
    }

}