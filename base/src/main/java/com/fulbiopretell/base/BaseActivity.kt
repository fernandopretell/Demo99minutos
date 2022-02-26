package com.fulbiopretell.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.fulbiopretell.base_ui.loading.LoadingView

open class BaseActivity : AppCompatActivity() {

    protected var ctx: Context? = null
    private val progressDialog: ProgressDialog? = null
    private val numeroTarjeta = ""
    private val processRunning = false
    private val loadingView by lazy { LoadingView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransitionEnter()
    }

    private fun showProgressDialog() {
        //loadingView.setMessage(message)
        if (loadingView.parent == null) {
            addContentView(loadingView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
        }
    }

    fun showLoading(show: Boolean) {
        if (show) showProgressDialog() else dismissProgressDialog()
    }

    private fun dismissProgressDialog() = loadingView.removeFromParent()

    override fun finish() {
        super.finish()
        overridePendingTransitionExit()
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected fun overridePendingTransitionEnter() {
        overridePendingTransition(R.animator.slide_from_right, R.animator.slide_to_left)
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected fun overridePendingTransitionExit() {
        overridePendingTransition(R.animator.slide_from_left, R.animator.slide_to_right)
    }

}