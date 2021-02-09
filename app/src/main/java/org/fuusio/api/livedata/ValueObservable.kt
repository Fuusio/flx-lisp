package org.fuusio.api.livedata

import androidx.lifecycle.LiveData

open class ValueObservable<T> : LiveData<T>() {

    fun get(): T = value!!

    fun set(value: T) {
        val currentValue = getValue()
        if (currentValue == null || value != currentValue) postValue(value)
    }
}