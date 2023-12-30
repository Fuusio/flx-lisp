package org.fuusio.api.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.flx.lisp.app.R

class ThemedBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var callback: BottomSheetBehavior.BottomSheetCallback
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = BottomSheetDialog(requireContext(), theme)

        bottomSheetDialog.setOnShowListener {
            callback = object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) bottomSheetDialog.dismiss()
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if(!slideOffset.isNaN()) bottomSheetDialog.window?.setDimAmount(0.5f - ((slideOffset * -1)/2))
                }
            }
            bottomSheetDialog.behavior.addBottomSheetCallback(callback)
        }
        return bottomSheetDialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        bottomSheetDialog.behavior.removeBottomSheetCallback(callback)
    }
}