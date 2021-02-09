/*
 * Copyright (C) 2001 - 2021 Marko Salmela
 *
 * http://fuusio.org
 *
 * All rights reserved.
 */
package com.flx.features.about.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flx.app.FlxReplApp

class AboutViewModel : ViewModel() {

    private val _versionText = MutableLiveData<String>().apply {
        value = FlxReplApp.getVersion()
    }

    val versionText: LiveData<String> = _versionText
}