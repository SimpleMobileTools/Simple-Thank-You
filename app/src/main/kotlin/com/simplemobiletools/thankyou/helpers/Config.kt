package com.simplemobiletools.thankyou.helpers

import android.content.Context
import com.simplemobiletools.commons.helpers.BaseConfig

class Config(context: Context) : BaseConfig(context) {
    companion object {
        fun newInstance(context: Context) = Config(context)
    }

    var hideLauncherIcon: Boolean
        get() = prefs.getBoolean(HIDE_LAUNCHER_ICON, false)
        set(hideLauncherIcon) = prefs.edit().putBoolean(HIDE_LAUNCHER_ICON, hideLauncherIcon).apply()
}
