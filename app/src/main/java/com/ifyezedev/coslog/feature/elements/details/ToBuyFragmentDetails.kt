package com.ifyezedev.coslog.feature.elements.details

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.data.db.entities.elementsBuilder
import com.ifyezedev.coslog.databinding.FragmentToBuyBinding

/**
 * This is the to-buy elements fragment which contains both the top and bottom parts of
 * the element details layout. Because the bottom part is the same for both
 * to-buy and to-make fragments we can reuse it. To get the bottom call [bottomBinding].
 * */
class ToBuyFragmentDetails : ElementsDetailsFragment<FragmentToBuyBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_to_buy

    private val nameInputIsEmpty get() = binding.nameValue.text.toString().isEmpty()
    private val costInputIsEmpty get() = binding.costValue.text.toString().isEmpty()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottomBinding.recyclerView
        binding.costValue.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            if (costInputIsEmpty)
                binding.costValue.setText(R.string.default_cost)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSaveButtonPressed() = with(binding) {
        if (nameInputIsEmpty) {
            toastNotify("Name input field cannot be empty")
            return
        }
        if (costInputIsEmpty) {
            toastNotify("Cost input field cannot be empty")
            return
        }

        val elementTmp = elementsBuilder {
            // if we are not creating a new element then we should keep the old element id
            if (!willInsertNewElement)
                eid = element!!.eid

            name = nameValue.text.toString()
            source = sourceValue.text.toString()
            cost = costValue.text.toString().toDouble()
            notes = bottomView.notes.text.toString()
            images = ArrayList(adapter.getFilePaths())
            isBuy = true
        }

        if (willInsertNewElement)
            detailsViewModel.insertElementInDatabase(elementTmp)
        else {
            detailsViewModel.updateElementInDatabase(elementTmp)
        }

        super.onSaveButtonPressed()
    }

    /**
     * This method would initialize all of the fragment`s UI input fields.
     * The method itself is called in onCreateView() in the super class only if
     * we are about to update an [Element].
     * */
    override fun onUpdateElement(element: Element) {
        super.onUpdateElement(element)
        binding.nameValue.setText(element.name)
        binding.sourceValue.setText(element.source)
        binding.costValue.setText(element.cost.toString())
        binding.bottomView.notes.setText(element.notes)
    }
}