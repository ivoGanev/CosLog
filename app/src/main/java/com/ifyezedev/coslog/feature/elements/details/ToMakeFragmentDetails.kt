package com.ifyezedev.coslog.feature.elements.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.data.db.entities.elementsBuilder
import com.ifyezedev.coslog.databinding.FragmentToMakeBinding
import java.lang.NumberFormatException

/**
 * This is the to-make details fragment which contains both the top and bottom parts of
 * the element details layout. Because the bottom part is the same for both
 * to-buy and to-make fragments we can reuse it. To get the bottom layout use [bottomBinding].
 * To get the underlying view model use [detailsViewModel]
 * */
class ToMakeFragmentDetails : ElementsDetailsFragment<FragmentToMakeBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_to_make

    private val nameInputIsEmpty get() = binding.nameValue.text.toString().isEmpty()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // make sure that the title is set after the view has been created
        // because by default we are setting it to the app's name in onAfterBindingCreated()
        // in the base fragment
        actionBar.setTitle(R.string.to_make_title)
    }

    override fun onAfterBindingCreated(view: View) {
        super.onAfterBindingCreated(view)
        binding.progressValue.adapter = ProgressSpinnerAdapter.create(requireContext())
        binding.timeValueLayout.setEndIconOnClickListener { onTimeButtonClicked() }
    }

    @SuppressLint("SetTextI18n")
    private fun onTimeButtonClicked() {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(0)
                .setMinute(0)
                .setTitleText("Select time")
                .build()
        picker.show(childFragmentManager, "time-picker")
        picker.addOnDismissListener {
            binding.timeValue.setText("${picker.hour} : ${picker.minute}")
        }
    }

    override fun onSaveButtonPressed() = with(binding) {
        if (nameInputIsEmpty) {
            toastNotify("Name field cannot be empty")
            return
        }

        val elementTmp = elementsBuilder {
            if (element != null)
                eid = element!!.eid

            name = nameValue.text.toString()

            try {
                time = timeValue.toString().toLong()
            } catch (ex: NumberFormatException) {
                Log.e(this::class.java.simpleName, ex.message!!)
            }

            progress = progressValue.selectedItem.toString()
            notes = bottomView.notes.text.toString()
            images = ArrayList(adapter.getFilePaths())
            isBuy = false
        }

        if (element == null)
            detailsViewModel.insertElementInDatabase(elementTmp)
        else {
            detailsViewModel.updateElementInDatabase(elementTmp)
        }

        super.onSaveButtonPressed()
    }


    override fun onUpdateElement(element: Element) = with(binding) {
        super.onUpdateElement(element)
        val progressResource = requireContext().resources.getStringArray(R.array.progress_array)
        progressValue.setSelection(progressResource.indexOf(element.progress))

        nameValue.setText(element.name)
        timeValue.setText(element.time.toString())
        bottomView.notes.setText(element.notes)
    }
}