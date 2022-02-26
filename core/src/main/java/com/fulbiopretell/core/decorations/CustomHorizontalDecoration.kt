package com.fulbiopretell.demo99minutos.core.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomHorizontalDecoration(
    private val margin: Int,
    private val multiplyEdges: Int = 3
) : RecyclerView.ItemDecoration() {

    override
    fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        if (parent.adapter?.itemCount == 1) {
            outRect.set(margin, margin, margin, margin)
            return
        }

        when (parent.getChildAdapterPosition(view)) {
            0 -> outRect.set(margin * multiplyEdges, margin, margin, margin)
            parent.adapter!!.itemCount - 1 -> outRect.set(0, margin, margin * multiplyEdges, margin)
            else -> outRect.set(0, margin, margin, margin)
        }
    }

}
