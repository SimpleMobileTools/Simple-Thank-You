package com.simplemobiletools.thankyou.activities

import android.os.Bundle
import com.simplemobiletools.commons.dialogs.ConfirmationDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.NavigationIcon
import com.simplemobiletools.thankyou.BuildConfig
import com.simplemobiletools.thankyou.R
import com.simplemobiletools.thankyou.databinding.ActivitySettingsBinding
import com.simplemobiletools.thankyou.extensions.config
import java.util.*

class SettingsActivity : SimpleActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setupToolbar(binding.settingsToolbar, NavigationIcon.Arrow)

        setupCustomizeColors()
        setupUseEnglish()
        setupHideLauncherIcon()
        updateTextColors(binding.settingsNestedScrollview)

        arrayOf(binding.settingsColorCustomizationLabel, binding.settingsGeneralSettingsLabel).forEach {
            it.setTextColor(getProperPrimaryColor())
        }

        arrayOf(binding.settingsColorCustomizationHolder, binding.settingsGeneralSettingsHolder).forEach {
            it.background.applyColorFilter(getProperBackgroundColor().getContrastColor())
        }
    }

    private fun setupCustomizeColors() {
        binding.settingsCustomizeColorsHolder.setOnClickListener {
            startCustomizationActivity()
        }
    }

    private fun setupUseEnglish() {
        binding.settingsUseEnglishHolder.beVisibleIf(config.wasUseEnglishToggled || Locale.getDefault().language != "en")
        binding.settingsUseEnglish.isChecked = config.useEnglish

        if (binding.settingsUseEnglishHolder.isGone()) {
            binding.settingsHideLauncherIconHolder.background = resources.getDrawable(R.drawable.ripple_all_corners, theme)
        }

        binding.settingsUseEnglishHolder.setOnClickListener {
            binding.settingsUseEnglish.toggle()
            config.useEnglish = binding.settingsUseEnglish.isChecked
            System.exit(0)
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
