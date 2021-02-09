package org.fuusio.api.view

import android.app.Dialog
import android.content.DialogInterface
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import com.flx.app.R
import com.flx.app.databinding.BottomSheetConfirmationBinding
import org.koin.core.KoinComponent

class ConfirmationBottomSheet(
    private val fragment: ViewBindingFragment<*>,
    @StringRes private val titleResId: Int,
    private val confirmationMessage: String,
    @StringRes private val negativeButtonTextResId: Int = R.string.action_cancel,
    @StringRes private val positiveButtonTextResId: Int = R.string.action_ok,
    private val onClickListener: DialogInterface.OnClickListener
) : KoinComponent {

    constructor(
        fragment: ViewBindingFragment<*>,
        @StringRes titleResId: Int,
        @StringRes confirmationMessageResId: Int,
        @StringRes negativeButtonTextResId: Int = R.string.action_cancel,
        @StringRes positiveButtonTextResId: Int = R.string.action_ok,
        onClickListener: DialogInterface.OnClickListener
    ) : this(
        fragment,
        titleResId,
        fragment.resources.getString(confirmationMessageResId),
        negativeButtonTextResId,
        positiveButtonTextResId,
        onClickListener
    )

    private val activity = fragment.requireActivity()
    private val resources = fragment.resources

    private lateinit var negativeButton: Button
    private lateinit var positiveButton: Button
    private lateinit var messageTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var dialog: Dialog

    private fun onCreate() {
        val binding = BottomSheetConfirmationBinding.inflate(activity.layoutInflater)
        negativeButton = binding.buttonNegative
        positiveButton = binding.buttonPositive
        messageTextView = binding.textViewConfirmationMessage
        titleTextView = binding.textViewConfirmationTitle

        negativeButton.text = getString(negativeButtonTextResId)
        positiveButton.text = getString(positiveButtonTextResId)

        val formattedText = fragment.formatMarkupText(confirmationMessage)
        messageTextView.text = formattedText
        titleTextView.text = getString(titleResId)

        dialog = fragment.showBottomSheetDialog(binding.root, null)

        negativeButton.setOnClickListener {
            dialog.dismiss()
            onClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE)
        }

        positiveButton.setOnClickListener {
            dialog.dismiss()
            onClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
        }
    }

    fun show() {
        onCreate()
    }

    private fun getString(@StringRes stringRes: Int): String = resources.getString(stringRes)
}