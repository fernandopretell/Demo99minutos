package com.fulbiopretell.demo99minutos.core.decorations

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpaceLinearInsideDecoration(
    context: Context,
    @DimenRes spacingHorizontalRes: Int,
    @DimenRes spacingVerticalRes: Int = spacingHorizontalRes
) : RecyclerView.ItemDecoration() {

    private val spacingHorizontal: Int = context.resources.getDimensionPixelSize(spacingHorizontalRes)
    private val spacingVertical: Int = context.resources.getDimensionPixelSize(spacingVerticalRes)

    private var orientation = -1

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        val itemCount = state.itemCount

        if (itemPosition == RecyclerView.NO_POSITION) return

        if (orientation == -1) getOrientation(parent)

        when (orientation) {
            LinearLayoutManager.VERTICAL -> {
                val padding = spacingVertical / 2
                outRect.left = spacingHorizontal
                outRect.right = spacingHorizontal
                when {
                    isFirstPosition(itemPosition) -> {
                        outRect.top = 0
                        outRect.bottom = padding
                    }
                    isLastPosition(itemPosition, itemCount) -> {
                        outRect.top = padding
                        outRect.bottom = 0
                    }
                    else -> {
                        outRect.top = padding
                        outRect.bottom = padding
                    }
                }
            }
            LinearLayoutManager.HORIZONTAL -> {
                val padding = spacingHorizontal / 2
                outRect.top = spacingVertical
                outRect.bottom = spacingVertical
                when {
                    isFirstPosition(itemPosition) -> {
                        outRect.left = 0
                        outRect.right = padding
                    }
                    isLastPosition(itemPosition, itemCount) -> {
                        outRect.left = padding
                        outRect.right = 0
                    }
                    else -> {
                        outRect.left = padding
                        outRect.right = padding
                    }
                }
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

    private fun isFirstPosition(position: Int): Boolean = (position == 0)

    private fun isLastPosition(position: Int, count: Int): Boolean = (count > 0 && position == count - 1)
}
