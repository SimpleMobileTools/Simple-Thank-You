package com.simplemobiletools.thankyou.helpers

import android.content.Context
import com.simplemobiletools.commons.extensions.sharedPreferencesCallback
import com.simplemobiletools.commons.helpers.BaseConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class Config(context: Context) : BaseConfig(context) {
    companion object {
        fun newInstance(context: Context) = Config(context)
    }

    var hideLauncherIcon: Boolean
        get() = prefs.getBoolean(HIDE_LAUNCHER_ICON, false)
        set(hideLauncherIcon) = prefs.edit().putBoolean(HIDE_LAUNCHER_ICON, hideLauncherIcon).apply()

    val hideLauncherIconFlow: Flow<Boolean> = prefs.run { sharedPreferencesCallback { hideLauncherIcon } }.filterNotNull()
    val wasUseEnglishToggledFlow: Flow<Boolean> = prefs.run { sharedPreferencesCallback { wasUseEnglishToggled } }.filterNotNull()
    val useEnglishFlow: Flow<Boolean> = prefs.run { sharedPreferencesCallback { useEnglish } }.filterNotNull()
}
