package com.ifyezedev.coslog.feature.elements.details

import android.content.Context
import android.widget.ArrayAdapter
import com.ifyezedev.coslog.R

/**
 * Class containing a factory method with the simple responsibility for creating the progress spinner adapter.
 * */
// Why like this and not directly in the fragment or activity?
// Because if the creation logic changes then this will be the only place to edit it;
// furthermore if we have to create this adapter in another class we won't have to repeat ourselves.
class ProgressSpinnerAdapter {
    companion object {
        fun create(context: Context) =
            ArrayAdapter.createFromResource(context, R.array.progress_array, android.R.layout.simple_list_item_1)
    }
}