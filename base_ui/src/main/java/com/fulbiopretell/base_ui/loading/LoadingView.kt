package com.fulbiopretell.base_ui.loading

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.fulbiopretell.base_ui.R

class LoadingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate()
    }

    private fun inflate() {
        LayoutInflater.from(context).inflate(R.layout.loading, this)
    }

    fun removeFromParent() {
        this.parent?.let {
            (it as ViewGroup).removeView(this)
        }
    }
}
