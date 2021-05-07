package com.ifyezedev.coslog.core.etc

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.R

/**
 * This is used to offset the items of the recycler view to start from the middle
 * instead from the most left side.
 * */
class BoundsOffsetDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)

        // It is crucial to refer to layoutParams.width
        // (view.width is 0 at this time)!
        val itemWidth = view.layoutParams.width
        val parentWidth = parent.width
        val offset = (parentWidth - itemWidth) / 2
        // println(view.width)
        if (itemPosition == 0) {
            outRect.left = offset
        } else if (itemPosition == state.itemCount - 1) {
            outRect.right = offset
        }
    }
}