package com.ifyezedev.coslog

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.ifyezedev.coslog.CosplayToolbarController.ToolbarType
import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.common.BaseFragment


abstract class CosplayBaseFragment<T : ViewDataBinding> : BaseFragment<T>() {

    private val compositionRoot: CosplayFragmentCompositionRoot by lazy {
        CosplayFragmentCompositionRoot(this)
    }

    lateinit var cosplayController: NavController
    lateinit var activity: CosplayActivity
    lateinit var cosplayToolbarController: CosplayToolbarController

    override fun onStart() {
        super.onStart()
        inject()
        cosplayToolbarController.displayAppBar(getToolbarType())
        cosplayToolbarController.getToolbar(getToolbarType()).setBackButtonListener { backButton ->
            onBackButtonPressed(backButton)
        }
    }

    private fun inject() {
        cosplayController = compositionRoot.cosplayController
        cosplayToolbarController = compositionRoot.cosplayToolbarController
    }

    abstract override fun bindingLayoutId(): Int

    /**
     * Defines which app bar the fragment would use. Default is defined in app_bar_cosplay.xml
     * */
    open fun getToolbarType(): ToolbarType {
        return ToolbarType.Cosplay
    }

    /**
     * If you need to do go back a few fragments you can override this and use the
     * cosplayController.
     * */
    open fun onBackButtonPressed(backButton: View) {
        cosplayController.popBackStack()
    }
}

class CosplayFragmentCompositionRoot(fragment: Fragment) {
    val application = fragment.requireActivity().application as BaseApplication

    private val activityCompositionRoot = (fragment.requireActivity() as CosplayActivity).cosplayCompositionRoot

    val cosplayController: NavController = activityCompositionRoot.cosplayController

    val cosplayToolbarController: CosplayToolbarController =
        activityCompositionRoot.cosplayToolbarController
}
