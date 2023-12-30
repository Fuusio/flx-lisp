package org.fuusio.api.view

import android.app.Dialog
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.fuusio.api.component.BottomSheetCallback
import org.fuusio.flx.core.util.Literals
import com.flx.lisp.app.R
import com.flx.lisp.app.databinding.BottomSheetActionsBinding

abstract class ActionsBottomSheet(
    fragment: ToolBarFragment<*>,
    private val callback: BottomSheetCallback
) : AbstractBottomSheet<ToolBarFragment<*>>(fragment) {

    private var actionCounter = 0

    private lateinit var actionsLayout: LinearLayout
    private lateinit var dialog: Dialog

    protected open fun onCreate() {
        val binding = BottomSheetActionsBinding.inflate(activity.layoutInflater)
        actionsLayout = binding.layoutActions
        actionsLayout.setPadding(16, 0, 16, 0)

        val title = getTitle()
        if (title.isNotBlank()) addTitle(title)

        createActions()

        if (actionCounter == 0) {
            action(
                R.drawable.icx_no_actions_available_24dp,
                R.string.text_no_actions_available
            ) {}
        }

        dialog = fragment.showBottomSheetDialog(binding.root, null)
        dialog.setOnDismissListener { callback.onDismissed() }
    }

    protected open fun getTitle() = Literals.EMPTY_STRING

    protected abstract fun createActions()

    protected open fun addTitle(title: String) {
        val titleTextView = TextView(context)
        titleTextView.text = title
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f)
        titleTextView.setTextColor(getColor(R.color.titleAndIconColor))
        titleTextView.gravity = Gravity.START or Gravity.CENTER_VERTICAL
        titleTextView.setTypeface(null, Typeface.BOLD)

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(64, 40, 16, 16)
        actionsLayout.addView(titleTextView, layoutParams)
    }

    protected open fun action(
        @DrawableRes iconRes: Int,
        @StringRes textRes: Int,
        listener: (View) -> Unit
    ) {
        val actionButton = ActionButton(context, iconRes, textRes)
        actionButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        actionButton.setOnClickListener{ view ->
            listener(view)
            dismiss()
        }
        actionsLayout.addView(actionButton)
        actionCounter++
    }

    protected fun dismiss() {
        dialog.dismiss()
    }

    open fun show() {
        onCreate()
    }
}