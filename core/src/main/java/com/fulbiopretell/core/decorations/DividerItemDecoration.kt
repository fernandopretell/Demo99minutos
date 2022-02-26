package com.fulbiopretell.demo99minutos.core.decorations

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.fulbiopretell.core.R
import com.fulbiopretell.demo99minutos.core.extensions.px

class DividerItemDecoration(
    context: Context,
    private val mLeftInset : Int = 0,
    private val mRightInset : Int = 0
) : RecyclerView.ItemDecoration() {

    private val divider: Drawable? = AppCompatResources.getDrawable(context, R.drawable.line_divider)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = mLeftInset.px + parent.paddingLeft
        val right = parent.width - mRightInset.px - parent.paddingRight
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + divider!!.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }
}
