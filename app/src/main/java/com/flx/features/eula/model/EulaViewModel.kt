package com.flx.features.eula.model

import androidx.lifecycle.ViewModel
import com.flx.app.logic.UserInfoManager
import com.flx.model.user.UserInfo
import org.koin.core.KoinComponent
import org.koin.core.inject

class EulaViewModel : ViewModel(), KoinComponent {

    private val userInfoManager: UserInfoManager by inject()

    fun acceptEula() {
        userInfoManager.acceptEula()
    }

    fun declineEula() {
        userInfoManager.declineEula()
    }

    fun getUserInfo(): UserInfo = userInfoManager.userInfo
}