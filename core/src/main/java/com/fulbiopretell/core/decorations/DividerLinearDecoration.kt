package com.fulbiopretell.demo99minutos.core.decorations

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DividerLinearDecoration(
    private val divider: Drawable?,
    private val showFirstDivider: Boolean = false,
    private val showLastDivider: Boolean = true
) : RecyclerView.ItemDecoration() {

    private var orientation = -1

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (divider == null)
            return

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION || itemPosition == 0 && !showFirstDivider) {
            return
        }

        if (orientation == -1) getOrientation(parent)

        if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.top = divider.intrinsicHeight
            if (showLastDivider && itemPosition == state.itemCount - 1) {
                outRect.bottom = outRect.top
            }
        } else {
            outRect.left = divider.intrinsicWidth
            if (showLastDivider && itemPosition == state.itemCount - 1) {
                outRect.right = outRect.left
            }
        }
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (divider == null) {
            super.onDrawOver(canvas, parent, state)
            return
        }

        var left = 0
        var right = 0
        var top = 0
        var bottom = 0
        val size: Int
        val orientation = if (orientation != -1) orientation else getOrientation(parent)
        val childCount = parent.childCount

        if (orientation == LinearLayoutManager.VERTICAL) {
            size = divider.intrinsicHeight
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
        } else {
            size = divider.intrinsicWidth
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
        }

        for (i in (if (showFirstDivider) 0 else 1) until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            if (orientation == LinearLayoutManager.VERTICAL) {
                top = child.top - params.topMargin - size
                bottom = top + size
            } else {
                left = child.left - params.leftMargin
                right = left + size
            }
            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }

        if (showLastDivider && childCount > 0) {
            val child = parent.getChildAt(childCount - 1)
            if (parent.getChildAdapterPosition(child) == state.itemCount - 1) {
                val params = child.layoutParams as RecyclerView.LayoutParams
                if (orientation == LinearLayoutManager.VERTICAL) {
                    top = child.bottom + params.bottomMargin
                    bottom = top + size
                } else {
                    left = child.right + params.rightMargin
                    right = left + size
                }
                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        if (orientation == -1) {
            if (parent.layoutManager is LinearLayoutManager) {
                val layoutManager = parent.layoutManager as LinearLayoutManager?
                orientation = layoutManager!!.orientation
            } else {
                throw IllegalStateException(
                    "DividerItemDecoration can only be used with a LinearLayoutManager."
                )
            }
        }
        return orientation
    }
}
