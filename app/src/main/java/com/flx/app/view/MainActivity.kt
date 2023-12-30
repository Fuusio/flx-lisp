/*
 * Copyright (C) 2016 - 2021 Marko Salmela
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
package com.flx.app.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.flx.lisp.app.R
import com.flx.lisp.app.databinding.MainWithAdsActivityBinding
import com.flx.app.logic.UserInfoManager
import com.flx.app.model.FlxAppState
import com.flx.app.model.FlxAppStore
import com.flx.features.start.view.StartActivity
import com.flx.model.user.UserInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.fuusio.api.redux.Action
import org.fuusio.api.redux.StateObserver
import org.fuusio.api.redux.Subscriber
import org.fuusio.api.redux.Subscription
import org.koin.core.KoinComponent
import org.koin.core.inject


class MainActivity : AppCompatActivity(), KoinComponent, StateObserver<FlxAppState> {

    private var navController: NavController? = null
    private var isEulaDeclined = false
    private var subscription: Subscription<FlxAppState>? = null
    private var userInfo: UserInfo? = null

    private val appStore: FlxAppStore by inject()
    private val userInfoManager: UserInfoManager by inject()
    private val subscriber: Subscriber<FlxAppState> = appStore.getSubscriber()

    private lateinit var binding: MainWithAdsActivityBinding
    private lateinit var navHostFragment: NavHostFragment

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainWithAdsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = NavHostFragment.create(R.navigation.mobile_navigation)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, navHostFragment)
                .setPrimaryNavigationFragment(navHostFragment)
                .commit()
        }
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_IS_USER_GUIDE_OPENED && resultCode == Activity.RESULT_OK) {
            isEulaDeclined = data?.getBooleanExtra(KEY_IS_EULA_DECLINED, false) ?: false
        }
    }

    // workaround for https://issuetracker.google.com/issues/142847973
    private fun getNavController(): NavController {
        return try {
            navHostFragment.findNavController()
        } catch (e: Exception) {
            navHostFragment = NavHostFragment.create(R.navigation.mobile_navigation)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, navHostFragment)
                .setPrimaryNavigationFragment(navHostFragment)
                .commit()
            navHostFragment.navController
        }
    }

    @ExperimentalCoroutinesApi
    override fun onResume() {
        super.onResume()

        if (isEulaDeclined) {
            finish()
            return
        }

        if (navController == null) {
            val navController = getNavController()
            binding.navView.setupWithNavController(navController)
            this.navController = navController
        }

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        if (!userInfoManager.isEulaAccepted && !preferences.getBoolean(UserInfo.PREFERENCE_EULA_ACCEPTED, false)) {
            val intent = Intent(this, StartActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_IS_USER_GUIDE_OPENED)
        } else {
            subscription = subscriber(::onStateChanged)
            getNavController().navigate(R.id.nav_repl)
        }
    }

    override fun onStop() {
        super.onStop()
        subscription?.unsubscriber?.invoke(this)
    }

    @ExperimentalCoroutinesApi
    @Suppress("UNUSED_PARAMETER")
    private fun onStateChanged(state: FlxAppState, action: Action) {

        userInfo = state.userInfo
    }

    override fun onSupportNavigateUp() = navController!!.navigateUp(binding.drawerLayout) || super.onSupportNavigateUp()

    override fun invoke(state: FlxAppState, action: Action) {}

    companion object {
        const val REQUEST_CODE_IS_USER_GUIDE_OPENED = 0xF001
        const val KEY_IS_EULA_DECLINED = "key.IsEulaAccepted"
    }
}
