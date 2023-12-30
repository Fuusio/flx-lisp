package org.fuusio.api.feature

import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.koin.core.context.GlobalContext
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.reflect.KClass

abstract class ViewFeature(@IdRes navId: Int) : Feature(navId)
