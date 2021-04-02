package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

/**
 * All fragments in the scope of the cosplay graph should inherit from this class to gain
 * quick access to services related to them, e.g. activity and nav controller.
 * Currently all services are manually injected.
 */
abstract class CosplayGraphBaseFragment<T : ViewDataBinding?> : BindingFragment<T>() {
    abstract override fun bindingLayoutId(): Int

    protected val activity: CosplayActivity get() = requireActivity() as CosplayActivity

    protected val cosplayController: NavController get() = activity.cosplayController
}