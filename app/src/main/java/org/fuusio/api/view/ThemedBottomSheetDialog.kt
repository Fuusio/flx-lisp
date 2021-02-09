package org.fuusio.api.view

import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.flx.app.R

class ThemedBottomSheetDialog(context: Context) : BottomSheetDialog(context, R.style.BottomSheetDialogTheme) {

    private val callback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) dismiss()
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            if(!slideOffset.isNaN()) window?.setDimAmount(0.5f - ((slideOffset * -1)/2))
        }
    }

    init {
        setOnShowListener {
            behavior.addBottomSheetCallback(callback)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        behavior.removeBottomSheetCallback(callback)
    }
}