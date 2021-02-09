package org.fuusio.api.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewMargin(private val margin: Int, private val columns: Int = 1)
    : RecyclerView.ItemDecoration() {

    init {
        require(margin >= 0)
        require(columns > 0)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        outRect.right = margin
        outRect.bottom = margin
        if (position < columns) outRect.top = margin
        if (position % columns == 0) outRect.left = margin
    }
}