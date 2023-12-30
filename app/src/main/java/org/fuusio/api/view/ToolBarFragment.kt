package org.fuusio.api.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.annotation.*
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.get
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.TransitionInflater
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.MaterialToolbar
import org.fuusio.api.extension.getColor
import org.fuusio.api.extension.setColorFilter
import com.flx.lisp.app.R

abstract class ToolBarFragment<T: ViewBinding> : ViewBindingFragment<T>() {

    private val navController get() = findNavController()

    private var optionsMenu: Menu? = null

    @MenuRes
    protected open fun getMenuRes(): Int? = null

    protected abstract fun getToolBar(): Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(getMenuRes() != null)
    }

    @CallSuper
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        optionsMenu = menu
        val menuRes = getMenuRes()
        if (menuRes != null) {
            inflater.inflate(menuRes, menu)
            onSetupOptionsMenu(menu)
        }
    }

    @CallSuper
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        for (i in 0 until menu.size()) {
            val item = menu[i]
            val icon = item.icon
            icon?.let { drawable ->
                val wrapped = DrawableCompat.wrap(drawable)
                drawable.mutate()
                DrawableCompat.setTint(wrapped, getColor(R.color.toolbarIconColor))
                item.icon = drawable

                if (item.hasSubMenu()) {
                    onPrepareOptionsMenu(item.subMenu!!)
                }
            }
        }
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        val toolBar = getToolBar()

        if (isSetupWithNavController()) {
            toolBar.setupWithNavController(findNavController())
        }
        onSetupToolBar(toolBar)

        if (toolBar is MaterialToolbar) {
            toolBar.navigationIcon?.setTint(getColor(R.color.navigationIcon))
        }
        getAppCompatActivity().setSupportActionBar(toolBar)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        return root
    }

    protected open fun isSetupWithNavController() = true

    protected open fun onSetupOptionsMenu(menu: Menu) {}

    protected open fun onSetupToolBar(toolBar: Toolbar) {}

    protected fun navigateTo(@IdRes navActionResId: Int, args: Bundle? = null, navOptions: NavOptions? = null, extras: Navigator.Extras? = null) {
        navController.navigate(navActionResId, args, navOptions, extras)
    }

    fun invalidateOptionsMenu() {
        activity?.invalidateOptionsMenu()
    }

    protected fun setMenuItemEnabled(@IdRes itemId: Int, enabled: Boolean) {
        optionsMenu?.let { optionsMenu ->
            val menuItem = optionsMenu.findItem(itemId)
            menuItem?. let { item ->
                item.isEnabled = enabled
                item.icon!!.mutate().alpha = if (enabled) 0xff else 0x44
            }
        }
    }

    @Suppress("SameParameterValue")
    protected fun setMenuItemTintColor(@IdRes itemId: Int, @ColorRes tintColor: Int) {
        optionsMenu?.let { optionsMenu ->
            val menuItem = optionsMenu.findItem(itemId)
            menuItem?. let { item ->
                val itemIcon = item.icon
                itemIcon?.let { icon ->
                    icon.mutate()
                    icon.setColorFilter(getColor(tintColor))
                }
            }
        }
    }

    fun showFeatureDisabledInformation(
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
        @IdRes navActionResId: Int
    ) {
        ConfirmationBottomSheet(
            this,
            titleResId,
            messageResId,
            onClickListener = { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> { navigateTo(navActionResId) }
                    else -> {}
                }
            }
        ).show()
    }
}