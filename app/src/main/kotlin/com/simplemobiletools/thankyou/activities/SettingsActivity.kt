package com.simplemobiletools.thankyou.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simplemobiletools.commons.compose.alert_dialog.rememberAlertDialogState
import com.simplemobiletools.commons.compose.extensions.enableEdgeToEdgeSimple
import com.simplemobiletools.commons.compose.theme.AppThemeSurface
import com.simplemobiletools.commons.dialogs.ConfirmationAdvancedAlertDialog
import com.simplemobiletools.commons.extensions.getAppIconColors
import com.simplemobiletools.commons.extensions.toggleAppIconColor
import com.simplemobiletools.commons.helpers.isTiramisuPlus
import com.simplemobiletools.thankyou.BuildConfig
import com.simplemobiletools.thankyou.R
import com.simplemobiletools.thankyou.extensions.config
import com.simplemobiletools.thankyou.extensions.launchChangeAppLanguageIntent
import com.simplemobiletools.thankyou.extensions.startCustomizationActivity
import com.simplemobiletools.thankyou.screens.SettingsScreen
import java.util.Locale
import kotlin.system.exitProcess

class SettingsActivity : ComponentActivity() {

    private val preferences by lazy { config }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()
        setContent {
            AppThemeSurface {
                val wasUseEnglishToggledFlow by preferences.wasUseEnglishToggledFlow.collectAsStateWithLifecycle(preferences.wasUseEnglishToggled)
                val useEnglishFlow by preferences.useEnglishFlow.collectAsStateWithLifecycle(preferences.useEnglish)
                val hideLauncherIconFlow by preferences.hideLauncherIconFlow.collectAsStateWithLifecycle(preferences.hideLauncherIcon)
                val displayLanguage = remember { Locale.getDefault().displayLanguage }
                val isUseEnglishEnabled by remember(wasUseEnglishToggledFlow) {
                    derivedStateOf {
                        (wasUseEnglishToggledFlow || Locale.getDefault().language != "en") && !isTiramisuPlus()
                    }
                }
                val confirmHideIconAlertDialogState = getConfirmHideIconAlertDialogState()

                SettingsScreen(
                    displayLanguage = displayLanguage,
                    isUseEnglishEnabled = isUseEnglishEnabled,
                    isUseEnglishChecked = useEnglishFlow,
                    onUseEnglishPress = { isChecked ->
                        preferences.useEnglish = isChecked
                        exitProcess(0)
                    },
                    onSetupLanguagePress = ::launchChangeAppLanguageIntent,
                    isHidingLauncherIcon = hideLauncherIconFlow,
                    hideLauncherIconClick = { isChecked ->
                        if (isChecked) {
                            confirmHideIconAlertDialogState.show()
                        } else {
                            toggleHideLauncherIcon()
                            preferences.hideLauncherIcon = false
                        }
                    },
                    customizeColors = ::startCustomizationActivity,
                    goBack = ::finish
                )
            }
        }
    }

    @Composable
    private fun getConfirmHideIconAlertDialogState() =
        rememberAlertDialogState().apply {
            DialogMember {
                ConfirmationAdvancedAlertDialog(
                    alertDialogState = this,
                    messageId = R.string.hide_launcher_icon_explanation,
                    positive = R.string.ok,
                    negative = R.string.cancel
                ) { hideIcon ->
                    preferences.hideLauncherIcon = hideIcon
                    if (hideIcon) {
                        toggleHideLauncherIcon()
                    }
                }
            }
        }

    private fun toggleHideLauncherIcon() {
        val appId = BuildConfig.APPLICATION_ID
        getAppIconColors().forEachIndexed { index, color ->
            toggleAppIconColor(appId, index, color, false)
        }
    }
}
