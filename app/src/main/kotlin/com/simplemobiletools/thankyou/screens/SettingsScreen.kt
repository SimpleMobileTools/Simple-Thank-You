package com.simplemobiletools.thankyou.screens

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.simplemobiletools.commons.R
import com.simplemobiletools.commons.compose.extensions.MyDevices
import com.simplemobiletools.commons.compose.settings.*
import com.simplemobiletools.commons.compose.settings.scaffold.SettingsScaffold
import com.simplemobiletools.commons.compose.theme.AppThemeSurface
import com.simplemobiletools.commons.compose.theme.SimpleTheme
import com.simplemobiletools.commons.compose.theme.divider_grey
import com.simplemobiletools.commons.helpers.isTiramisuPlus

@Composable
internal fun SettingsScreen(
    displayLanguage: String,
    isUseEnglishEnabled: Boolean,
    isUseEnglishChecked: Boolean,
    isHidingLauncherIcon: Boolean,
    onUseEnglishPress: (Boolean) -> Unit,
    onSetupLanguagePress: () -> Unit,
    hideLauncherIconClick: (Boolean) -> Unit,
    customizeColors: () -> Unit,
    goBack: () -> Unit,
) {
    SettingsScaffold(title = stringResource(id = R.string.settings), goBack = goBack) {
        SettingsGroup(title = {
            SettingsTitleTextComponent(text = stringResource(id = R.string.color_customization))
        }) {
            SettingsPreferenceComponent(
                label = stringResource(id = R.string.customize_colors),
                doOnPreferenceClick = customizeColors,
            )
        }
        SettingsHorizontalDivider()
        SettingsGroup(title = {
            SettingsTitleTextComponent(text = stringResource(id = R.string.general_settings))
        }) {

            if (isUseEnglishEnabled) {
                SettingsCheckBoxComponent(
                    label = stringResource(id = R.string.use_english_language),
                    initialValue = isUseEnglishChecked,
                    onChange = onUseEnglishPress,
                )
            }
            if (isTiramisuPlus()) {
                SettingsPreferenceComponent(
                    label = stringResource(id = R.string.language),
                    value = displayLanguage,
                    doOnPreferenceClick = onSetupLanguagePress,
                    preferenceLabelColor = SimpleTheme.colorScheme.onSurface,
                    preferenceValueColor = SimpleTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }
            SettingsCheckBoxComponent(
                label = stringResource(id = com.simplemobiletools.thankyou.R.string.hide_launcher_icon),
                initialValue = isHidingLauncherIcon,
                onChange = hideLauncherIconClick,
            )
        }
    }
}

@Composable
@MyDevices
private fun SettingsScreenPreview() {
    AppThemeSurface {
        SettingsScreen(
            displayLanguage = "English",
            isUseEnglishEnabled = false,
            isUseEnglishChecked = false,
            isHidingLauncherIcon = false,
            onUseEnglishPress = {},
            onSetupLanguagePress = {},
            hideLauncherIconClick = {},
            customizeColors = {},
            goBack = {},
        )
    }
}

