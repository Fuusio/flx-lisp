package org.fuusio.api.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.button.MaterialButton
import com.flx.lisp.app.databinding.ActionButtonBinding

@SuppressLint("ViewConstructor")
class ActionButton(context: Context, @DrawableRes iconRes: Int, @StringRes textRes: Int)
    : ConstraintLayout(context) {

    private val button: MaterialButton

    init{
        val binding = ActionButtonBinding.inflate(LayoutInflater.from(context), this, true)
        button = binding.buttonAction
        button.icon = ResourcesCompat.getDrawable(resources, iconRes, context.theme)
        button.text = resources.getString(textRes)
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        button.setOnClickListener(listener)
    }
}