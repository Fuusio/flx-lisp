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
package org.fuusio.api.util

import android.text.Editable
import android.text.TextWatcher

open class TextChangeObserver(val callback: (String) -> Unit) : TextWatcher {

    override fun afterTextChanged(editable: Editable?) {
        // Ignore
    }

    override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
        // Ignore
    }

    override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
        sequence?.let { callback(sequence.toString()) }
    }
}