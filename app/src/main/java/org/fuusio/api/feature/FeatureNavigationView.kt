package org.fuusio.api.feature

import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class FeatureNavigationView(context: Context,  attrs: AttributeSet? = null) : NavigationView(context, attrs) {

    override fun setNavigationItemSelectedListener(listener: OnNavigationItemSelectedListener?) {
        var actualListener = listener

        if (actualListener != null) {
            actualListener =
                OnNavigationItemSelectedListener { item ->
                    onNavigating(item)
                    listener?.onNavigationItemSelected(item) ?: false
                }
        }
        super.setNavigationItemSelectedListener(actualListener)
    }

    var activeFeature: ViewFeature? = null

    private fun onNavigating(menuItem: MenuItem) {
        val feature = Feature.get(menuItem.itemId)

        feature.let { activatedFeature ->
            if (activatedFeature != activeFeature) {

                require(activatedFeature is ViewFeature)

                activeFeature?.deactivate()
                activeFeature = activatedFeature
                activeFeature?.activate()
            }
        }
    }
}