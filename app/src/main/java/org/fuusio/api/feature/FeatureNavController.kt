package org.fuusio.api.feature

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.navigation.*

@SuppressLint("MissingSuperCall")
class FeatureNavController(private val peer: NavController, context: Context) : NavController(context) {

    override fun addOnDestinationChangedListener(listener: OnDestinationChangedListener) {
        peer.addOnDestinationChangedListener(listener)
    }

    override fun getNavInflater() = peer.navInflater

    override fun createDeepLink() = peer.createDeepLink()

    override fun restoreState(navState: Bundle?) {
        peer.restoreState(navState)
    }

    override fun saveState(): Bundle? {
        return peer.saveState()
    }

    override fun getViewModelStoreOwner(navGraphId: Int) = peer.getViewModelStoreOwner(navGraphId)

    override fun handleDeepLink(intent: Intent?) = peer.handleDeepLink(intent)

    override fun removeOnDestinationChangedListener(listener: OnDestinationChangedListener) {
        peer.removeOnDestinationChangedListener(listener)
    }

    override fun getBackStackEntry(destinationId: Int) = peer.getBackStackEntry(destinationId)

    override fun getGraph() = peer.graph

    override fun navigateUp() = peer.navigateUp()

    override fun navigate(resId: Int) {
        navigate(resId, null)
    }

    override fun navigate(resId: Int, args: Bundle?) {
        navigate(resId, args, null)
    }

    override fun navigate(resId: Int, args: Bundle?, navOptions: NavOptions?) {
        navigate(resId, args, navOptions, null)
    }

    override fun navigate(
        resId: Int,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        onNavigating(resId)
        peer.navigate(resId, args, navOptions, navigatorExtras)
    }

    override fun navigate(deepLink: Uri) {
        peer.navigate(deepLink)
    }

    override fun navigate(deepLink: Uri, navOptions: NavOptions?) {
        peer.navigate(deepLink, navOptions)
    }

    override fun navigate(
        deepLink: Uri,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        peer.navigate(deepLink, navOptions, navigatorExtras)
    }

    override fun navigate(directions: NavDirections) {
        peer.navigate(directions)
    }

    override fun navigate(directions: NavDirections, navOptions: NavOptions?) {
        peer.navigate(directions, navOptions)
    }

    override fun navigate(directions: NavDirections, navigatorExtras: Navigator.Extras) {
        peer.navigate(directions, navigatorExtras)
    }

    override fun popBackStack() = peer.popBackStack()

    override fun popBackStack(destinationId: Int, inclusive: Boolean) = peer.popBackStack(destinationId, inclusive)

    override fun getCurrentDestination() = peer.currentDestination

    override fun setGraph(graphResId: Int) {
        peer.setGraph(graphResId)
    }

    override fun setGraph(graphResId: Int, startDestinationArgs: Bundle?) {
        peer.setGraph(graphResId, startDestinationArgs)
    }

    override fun setGraph(graph: NavGraph) {
        peer.graph = graph
    }

    override fun setGraph(graph: NavGraph, startDestinationArgs: Bundle?) {
        peer.setGraph(graph, startDestinationArgs)
    }

    override fun getNavigatorProvider() = peer.navigatorProvider

    private var activeFeature: ViewFeature? = null

    private fun onNavigating(resId: Int) {
        val feature = Feature.get(resId)

        feature.let { activatedFeature ->
            require(activatedFeature is ViewFeature)
            activateViewFeature(activatedFeature)
        }
    }

    fun activateViewFeature(activatedFeature: ViewFeature) {
        if (activatedFeature != activeFeature) {
            activeFeature?.deactivate()
            activeFeature = activatedFeature
            activeFeature?.activate()
        }
    }
}