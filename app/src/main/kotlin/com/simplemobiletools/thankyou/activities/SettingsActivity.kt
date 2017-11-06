package com.simplemobiletools.thankyou.activities

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import com.simplemobiletools.commons.activities.BaseSimpleActivity
import com.simplemobiletools.commons.extensions.updateTextColors
import com.simplemobiletools.thankyou.R
import com.simplemobiletools.thankyou.extensions.config
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onResume() {
        super.onResume()

        setupCustomizeColors()
        setupHideLauncherIcon()
        updateTextColors(settings_holder)
    }

    private fun setupCustomizeColors() {
        settings_customize_colors_holder.setOnClickListener {
            startCustomizationActivity()
        }
    }

    private fun setupHideLauncherIcon() {
        settings_hide_launcher_icon.isChecked = config.hideLauncherIcon
        settings_hide_launcher_icon_holder.setOnClickListener {
            settings_hide_launcher_icon.toggle()
            config.hideLauncherIcon = settings_hide_launcher_icon.isChecked

            val componentName = ComponentName(this, MainActivity::class.java)
            val state = if (config.hideLauncherIcon) PackageManager.COMPONENT_ENABLED_STATE_DISABLED else PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            packageManager.setComponentEnabledSetting(componentName, state, PackageManager.DONT_KILL_APP)
        }
    }
}
