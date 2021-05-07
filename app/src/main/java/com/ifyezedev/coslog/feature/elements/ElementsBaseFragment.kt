package com.ifyezedev.coslog.feature.elements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.core.common.BaseFragment
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.feature.elements.ElementsAdapter.ElementsViewHolder
import java.lang.ClassCastException

/**
 * A base fragment that contains service dependencies for the element
 * list fragments contained within the view pager in [ElementsFragment]
 * */
abstract class ElementsBaseFragment<T : ViewDataBinding> :
    BaseFragment<T>() {
    abstract override fun bindingLayoutId(): Int

    protected lateinit var cosplayController: NavController
    protected lateinit var elementsViewModel : ElementsViewModel

    override fun onAfterBindingCreated(view: View) {
        super.onAfterBindingCreated(view)
        val elementsFragment: ElementsFragment
        try {
            elementsFragment = parentFragment as ElementsFragment
        }
        catch(ex: ClassCastException) {
            throw ClassCastException("This class depends on ElementFragment to be its parent.")
        }

        cosplayController = elementsFragment.cosplayController
        elementsViewModel = elementsFragment.viewModel

        elementsViewModel.elements.observe(viewLifecycleOwner) { elementsData ->
            onElementsUpdated(elementsData)
        }
    }

    /**
     * This method provides the list of database elements when they are being updated.
     * */
    protected open fun onElementsUpdated(elements: List<Element>) {
    }
}
