package org.fuusio.api.feature

import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.koin.core.context.GlobalContext
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.reflect.KClass

abstract class ViewFeature(@IdRes navId: Int) : Feature(navId) {

    fun <T: ViewModel> getViewModel(viewModelStoreOwner: ViewModelStoreOwner, viewModelClass: KClass<T>): T {
        activate()
        
        val factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return getKoin().get(viewModelClass, null, null)
            }
        }
        return ViewModelProvider(viewModelStoreOwner, factory).get(viewModelClass.java)
    }
}
