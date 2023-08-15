package com.simplemobiletools.thankyou.activities

import android.os.Bundle
import com.simplemobiletools.commons.dialogs.ConfirmationDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.NavigationIcon
import com.simplemobiletools.commons.helpers.isTiramisuPlus
import com.simplemobiletools.thankyou.BuildConfig
import com.simplemobiletools.thankyou.R
import com.simplemobiletools.thankyou.databinding.ActivitySettingsBinding
import com.simplemobiletools.thankyou.extensions.config
import java.util.Locale

class SettingsActivity : SimpleActivity() {
    private val binding by viewBinding(ActivitySettingsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            updateMaterialActivityViews(settingsCoordinator, settingsHolder, useTransparentNavigation = true, useTopSearchMenu = false)
            setupMaterialScrollListener(settingsNestedScrollview, settingsToolbar)
        }
    }

    override fun onResume() {
        super.onResume()
        setupToolbar(binding.settingsToolbar, NavigationIcon.Arrow)

        setupCustomizeColors()
        setupUseEnglish()
        setupLanguage()
        setupHideLauncherIcon()
        updateTextColors(binding.settingsNestedScrollview)

        binding.apply {
            arrayOf(settingsColorCustomizationSectionLabel, settingsGeneralSettingsLabel).forEach {
                it.setTextColor(getProperPrimaryColor())
            }
        }
    }

    private fun setupCustomizeColors() {
        binding.settingsColorCustomizationHolder.setOnClickListener {
            startCustomizationActivity()
        }
    }

    private fun setupUseEnglish() {
        binding.apply {
            settingsUseEnglishHolder.beVisibleIf((config.wasUseEnglishToggled || Locale.getDefault().language != "en") && !isTiramisuPlus())
            settingsUseEnglish.isChecked = config.useEnglish
            settingsUseEnglishHolder.setOnClickListener {
                settingsUseEnglish.toggle()
                config.useEnglish = settingsUseEnglish.isChecked
                System.exit(0)
            }
        }
    }

    private fun setupLanguage() {
        binding.apply {
            settingsLanguage.text = Locale.getDefault().displayLanguage
            settingsLanguageHolder.beVisibleIf(isTiramisuPlus())
            settingsLanguageHolder.setOnClickListener {
                launchChangeAppLanguageIntent()
            }
        }
    }

    private fun setupHideLauncherIcon() {
        binding.settingsHideLauncherIcon.isChecked = config.hideLauncherIcon
        binding.settingsHideLauncherIconHolder.setOnClickListener {
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
        binding.settingsHideLauncherIcon.toggle()
        config.hideLauncherIcon = binding.settingsHideLauncherIcon.isChecked

        val appId = BuildConfig.APPLICATION_ID
        getAppIconColors().forEachIndexed { index, color ->
            toggleAppIconColor(appId, index, color, false)
        }
    }
}
