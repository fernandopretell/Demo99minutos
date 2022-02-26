package com.fulbiopretell.demo99minutos.core.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fulbiopretell.demo99minutos.core.extensions.px

class MarginVerticalItemDecoration(
    private val marginVertical: Int,
    private val marginHorizontal: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when (parent.getChildAdapterPosition(view)) {
            0 -> outRect.set(marginHorizontal.px, marginVertical.px, marginHorizontal.px, marginVertical.px)
            else -> outRect.set(marginHorizontal.px, 0, marginHorizontal.px, marginVertical.px)
        }
    }
}

