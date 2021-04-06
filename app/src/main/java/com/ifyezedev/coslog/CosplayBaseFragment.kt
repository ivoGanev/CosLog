package com.ifyezedev.coslog

import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController

/**
 * All fragments in the scope of the cosplay graph should inherit from this class to gain
 * quick access to services related to them, e.g. activity and nav controller.
 * Currently all services are manually injected.
 */
abstract class CosplayBaseFragment<T : ViewDataBinding?> : BindingFragment<T>() {
    abstract override fun bindingLayoutId(): Int

    protected val activity: CosplayActivity get() = requireActivity() as CosplayActivity

    protected val cosplayNavController: NavController get() = activity.cosplayNavController

    /**
     * This controller can be accessed only
     * */
    protected val dialogsController: NavController get() = activity.dialogsController
}