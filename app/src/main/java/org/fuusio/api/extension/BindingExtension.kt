/*
 * Copyright (C) 2001 - 2020 Marko Salmela
 *
 * http://fuusio.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("unused")

package org.fuusio.api.extension

import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.fuusio.api.util.TextChangeObserver

fun <T> Fragment.observe(liveData: LiveData<T>, observer: (T) -> Unit) {
    liveData.observe(this.viewLifecycleOwner, Observer(observer))
}

fun EditText.updateText(text: String): Boolean
    = if (this.text.toString() != text) {
        this.setText(text)
        true
    } else false

fun EditText.onDoubleValueChanged(observer: (Double) -> Unit) {
    addTextChangedListener(TextChangeObserver { text ->
        val value = text.toDoubleOrNull()
        value?.let { observer(value) }
    })
}

fun EditText.onFloatValueChanged(observer: (Float) -> Unit) {
    addTextChangedListener(TextChangeObserver { text ->
        val value = text.toFloatOrNull()
        value?.let { observer(value) }
    })
}

fun EditText.onIntValueChanged(observer: (Int) -> Unit) {
    addTextChangedListener(TextChangeObserver { text ->
        val value = text.toIntOrNull()
        value?.let { observer(value) }
    })
}

fun EditText.onLongValueChanged(observer: (Long) -> Unit) {
    addTextChangedListener(TextChangeObserver { text ->
        val value = text.toLongOrNull()
        value?.let { observer(value) }
    })
}

fun EditText.onTextChanged(observer: (String) -> Unit) {
    addTextChangedListener(TextChangeObserver(observer))
}

fun Switch.onToggled(observer: (Boolean) -> Unit) {
    setOnCheckedChangeListener { _, isChecked -> observer(isChecked) }
}

fun Spinner.onValueChanged(observer: (Int) -> Unit) {
    val adapter = this.adapter!!
    onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            // Ignore
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (val item = adapter.getItem(position)) {
                is Int -> observer(item)
                is String -> observer(item.toInt())
                else -> throw IllegalStateException()
            }
        }
    }
}

fun Spinner.selectItem(item: Int) {
    val adapter = this.adapter!! as BaseAdapter

    for (i in 0 until count) {
        val intItem = when (val adapterItem = adapter.getItem(i)) {
            is String -> adapterItem.toInt()
            is Int -> adapterItem
            else -> throw IllegalStateException()
        }
        if (intItem == item) {
            this.setSelection(i)
            break
        }
    }
}

fun <T> Fragment.setObserver(liveData: LiveData<T>, observer: (T) -> Unit) {
    liveData.observe(this.viewLifecycleOwner, Observer<T>(observer))
}
