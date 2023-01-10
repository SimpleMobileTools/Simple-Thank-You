package com.simplemobiletools.thankyou.activities

import android.os.Bundle
import com.simplemobiletools.commons.dialogs.ConfirmationDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.NavigationIcon
import com.simplemobiletools.commons.helpers.isTiramisuPlus
import com.simplemobiletools.thankyou.BuildConfig
import com.simplemobiletools.thankyou.R
import com.simplemobiletools.thankyou.extensions.config
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*

class SettingsActivity : SimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        updateMaterialActivityViews(settings_coordinator, settings_holder, useTransparentNavigation = true, useTopSearchMenu = false)
        setupMaterialScrollListener(settings_nested_scrollview, settings_toolbar)
    }

    override fun onResume() {
        super.onResume()
        setupToolbar(settings_toolbar, NavigationIcon.Arrow)

        setupCustomizeColors()
        setupUseEnglish()
        setupLanguage()
        setupHideLauncherIcon()
        updateTextColors(settings_nested_scrollview)

        arrayOf(settings_color_customization_section_label, settings_general_settings_label).forEach {
            it.setTextColor(getProperPrimaryColor())
        }
    }

    private fun setupCustomizeColors() {
        settings_color_customization_holder.setOnClickListener {
            startCustomizationActivity()
        }
    }

    private fun setupUseEnglish() {
        settings_use_english_holder.beVisibleIf((config.wasUseEnglishToggled || Locale.getDefault().language != "en") && !isTiramisuPlus())
        settings_use_english.isChecked = config.useEnglish
        settings_use_english_holder.setOnClickListener {
            settings_use_english.toggle()
            config.useEnglish = settings_use_english.isChecked
            System.exit(0)
        }
    }

    private fun setupLanguage() {
        settings_language.text = Locale.getDefault().displayLanguage
        settings_language_holder.beVisibleIf(isTiramisuPlus())
        settings_language_holder.setOnClickListener {
            launchChangeAppLanguageIntent()
        }
    }

    private fun setupHideLauncherIcon() {
        settings_hide_launcher_icon.isChecked = config.hideLauncherIcon
        settings_hide_launcher_icon_holder.setOnClickListener {
            if (config.hideLauncherIcon) {
                toggleHideLauncherIcon()
            } else {
                ConfirmationDialog(this, "", R.string.hide_launcher_icon_explanation, R.string.ok, R.string.cancel) {
                    toggleHideLauncherIcon()
                }
            }
        }
    }

    private fun toggleHideLauncherIcon() {
        settings_hide_launcher_icon.toggle()
        config.hideLauncherIcon = settings_hide_launcher_icon.isChecked

        val appId = BuildConfig.APPLICATION_ID
        getAppIconColors().forEachIndexed { index, color ->
            toggleAppIconColor(appId, index, color, false)
        }
    }
}
