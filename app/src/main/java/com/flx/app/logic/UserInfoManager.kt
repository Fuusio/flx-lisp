package com.flx.app.logic

import androidx.preference.PreferenceManager
import com.flx.app.FlxReplApp
import com.flx.app.model.*
import com.flx.model.user.UserInfo
import org.fuusio.api.redux.Action
import org.fuusio.api.redux.Subscription

class UserInfoManager(private val appStore: FlxAppStore) {

    var isEulaAccepted = false

    private val subscription: Subscription<FlxAppState> = appStore.getSubscriber()(::onStateChanged)
    internal lateinit var userInfo: UserInfo

    fun getUserInfoFromDb() {
        subscription.dispatcher(GetUserInfo)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onStateChanged(state: FlxAppState, action: Action) {
        userInfo = state.userInfo

        when (action) {
            is AcceptEula, is DeclineEula -> appStore.saveUserInfo()
        }
    }

    fun acceptEula() {
        isEulaAccepted = true
        saveEulaAcceptedPreference()
        subscription.dispatcher(AcceptEula)
    }

    fun declineEula() {
        isEulaAccepted = false
        saveEulaAcceptedPreference()
        subscription.dispatcher(DeclineEula)
    }

    private fun saveEulaAcceptedPreference() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(FlxReplApp.instance.applicationContext)
        val edit = preferences.edit()
        edit.putBoolean(UserInfo.PREFERENCE_EULA_ACCEPTED, isEulaAccepted)
        edit.apply()
    }
}