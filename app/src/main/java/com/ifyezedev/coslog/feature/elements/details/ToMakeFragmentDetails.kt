package com.ifyezedev.coslog.feature.elements.details

import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.data.db.entities.elementsBuilder
import com.ifyezedev.coslog.databinding.FragmentToMakeBinding
import com.ifyezedev.coslog.feature.elements.details.ElementsDetailsFragment

/**
 * This is the to-make details fragment which contains both the top and bottom parts of
 * the element details layout. Because the bottom part is the same for both
 * to-buy and to-make fragments we can reuse it. To get the bottom layout use [bottomBinding].
 * To get the underlying view model use [detailsViewModel]
 * */
class ToMakeFragmentDetails : ElementsDetailsFragment<FragmentToMakeBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_to_make

    override fun onSaveButtonPressed() {
        detailsViewModel.saveBitmapsToInternalStorage(adapter.filterCachedBitmaps()) { savedFilesPath ->
            detailsViewModel.insertElement(elementsBuilder {
                name = binding.nameValue.text.toString()
                time = binding.timeValue.text.toString().toLong()
                progress = binding.progressValue.text.toString().toFloat()
                notes = binding.bottomView.notes.text.toString()
                images = ArrayList(savedFilesPath)
                isBuy = false
            })
        }
    }

    override fun setUpWithElement(element: Element) {
        super.setUpWithElement(element)
        binding.nameValue.setText(element.name)
        binding.timeValue.setText(element.time.toString())
        binding.progressValue.setText(element.progress.toString())
        binding.bottomView.notes.setText(element.notes)
    }
}