/*
 * Copyright (C) 2001 - 2021 Marko Salmela
 *
 * http://fuusio.org
 *
 * All rights reserved.
 */
package org.fuusio.api.view

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.flx.app.R
import com.flx.util.TextMarkupFormatter
import org.koin.core.KoinComponent

abstract class ViewBindingFragment<T: ViewBinding> : Fragment(), KoinComponent {

    protected val binding get() = _binding!!

    private var _binding: T? = null

    protected abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback { onBackPressed() }
    }

    open fun onBackPressed(): Boolean = false

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = createBinding(inflater, container)
        onSetupViewBindings(binding)
        return binding.root
    }

    protected fun getAppCompatActivity() = (requireActivity() as AppCompatActivity)

    protected abstract fun onSetupViewBindings(binding: T)

    fun setViewEnabled(view: View, enabled: Boolean) {
        view.isEnabled = enabled

        when (view) {
            is MaterialButton -> view.alpha = if (enabled) 1.0f else 0.25f
            is ImageButton -> view.imageAlpha = if (enabled) 0xff else 0x44
            is TextView -> view.alpha = if (enabled) 1.0f else 0.25f
            else -> {}
        }
    }

    fun showSnackbar(@StringRes messageResId: Int) {
        showSnackbar(getString(messageResId))
    }

    fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        val context = requireContext()
        snackbarView.background = ResourcesCompat.getDrawable(context.resources, R.drawable.bg_snackbar, context.theme)
        snackbar.show()
    }

    fun showSnackbar(message: SpannableStringBuilder) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }


    fun showKeyboard(editText: EditText, isForced: Boolean = false) {
        editText.requestFocus()
        val manager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (isForced) {
            manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } else {
            manager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hideKeyboard(editText: EditText? = null) {
        val activity = requireActivity()
        val focusedView = editText ?: activity.currentFocus
        focusedView?.let { view ->
            val manager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun formatMarkupText(text: String): SpannableStringBuilder =
            TextMarkupFormatter(requireContext()).format(text)

    protected fun showBottomSheetDialog(@LayoutRes sheetLayoutRes: Int, dismissListener: DialogInterface.OnDismissListener)
            = showBottomSheetDialog(requireActivity().layoutInflater.inflate(sheetLayoutRes, null), dismissListener)

    fun showBottomSheetDialog(sheetView: View, dismissListener: DialogInterface.OnDismissListener? = null): BottomSheetDialog {
        val activity = requireActivity()
        val dialog = ThemedBottomSheetDialog(activity)
        dismissListener?.let { dialog.setOnDismissListener(dismissListener) }
        dialog.setContentView(sheetView)
        val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        dialog.show()
        return dialog
    }
}