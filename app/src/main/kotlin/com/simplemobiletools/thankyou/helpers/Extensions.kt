package com.simplemobiletools.thankyou.helpers

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.models.Release
import com.simplemobiletools.thankyou.R

fun ComponentActivity.appLaunchedCompose(
    appId: String,
    showUpgradeDialog: () -> Unit,
    showDonateDialog: () -> Unit,
    showRateUsDialog: () -> Unit
) {
    baseConfig.internalStoragePath = getInternalStoragePath()
    updateSDCardPath()
    baseConfig.appId = appId
    if (baseConfig.appRunCount == 0) {
        baseConfig.wasOrangeIconChecked = true
        checkAppIconColor()
    } else if (!baseConfig.wasOrangeIconChecked) {
        baseConfig.wasOrangeIconChecked = true
        val primaryColor = resources.getColor(R.color.color_primary)
        if (baseConfig.appIconColor != primaryColor) {
            getAppIconColors().forEachIndexed { index, color ->
                toggleAppIconColor(appId, index, color, false)
            }

            val defaultClassName = "${baseConfig.appId.removeSuffix(".debug")}.activities.SplashActivity"
            packageManager.setComponentEnabledSetting(
                ComponentName(baseConfig.appId, defaultClassName),
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                PackageManager.DONT_KILL_APP
            )

            val orangeClassName = "${baseConfig.appId.removeSuffix(".debug")}.activities.SplashActivity.Orange"
            packageManager.setComponentEnabledSetting(
                ComponentName(baseConfig.appId, orangeClassName),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

            baseConfig.appIconColor = primaryColor
            baseConfig.lastIconColor = primaryColor
        }
    }

    baseConfig.appRunCount++
    if (baseConfig.appRunCount % 30 == 0 && !isAProApp()) {
        if (!resources.getBoolean(R.bool.hide_google_relations)) {
            if (getCanAppBeUpgraded()) {
                showUpgradeDialog()
            } else if (!isOrWasThankYouInstalled()) {
                showDonateDialog()
            }
        }
    }

    if (baseConfig.appRunCount % 40 == 0 && !baseConfig.wasAppRated) {
        if (!resources.getBoolean(R.bool.hide_google_relations)) {
            showRateUsDialog()
        }
    }
}

fun ComponentActivity.checkWhatsNewCompose(releases: List<Release>, currVersion: Int, showWhatsNewDialog: (List<Release>) -> Unit) {
    if (baseConfig.lastVersion == 0) {
        baseConfig.lastVersion = currVersion
        return
    }

    val newReleases = arrayListOf<Release>()
    releases.filterTo(newReleases) { it.id > baseConfig.lastVersion }

    if (newReleases.isNotEmpty()) {
        showWhatsNewDialog(newReleases)
    }

    baseConfig.lastVersion = currVersion
}

fun ComponentActivity.upgradeToPro() {
    launchViewIntent("https://simplemobiletools.com/upgrade_to_pro")
}
