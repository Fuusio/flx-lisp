/*
 * Copyright (C) 2001 - 2021 Marko Salmela
 *
 * http://fuusio.org
 *
 * All rights reserved.
 */
package com.flx.features.repl.widget

import android.content.Context
import android.text.InputType.*
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection


class FormEditText(context: Context, attributeSet: AttributeSet)
    : androidx.appcompat.widget.AppCompatEditText(context, attributeSet) {

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        val inputConnection: InputConnection = super.onCreateInputConnection(outAttrs)!!
        outAttrs.inputType = TYPE_TEXT_FLAG_NO_SUGGESTIONS or
                TYPE_TEXT_VARIATION_FILTER or
                TYPE_CLASS_TEXT or
                TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or
                TYPE_TEXT_VARIATION_PASSWORD
        outAttrs.privateImeOptions = "nm"
        return inputConnection
    }
}