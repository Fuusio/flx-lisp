package org.fuusio.api.model

import androidx.lifecycle.LiveData

class ModelObservable<T> : LiveData<T>() {

    fun get(): T = value!!

    fun set(value: T) {
        val currentValue = getValue()
        if (currentValue == null || value != currentValue) postValue(value)
    }

    fun hasValue() = value != null
}