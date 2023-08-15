package com.simplemobiletools.thankyou.activities

import android.content.Intent
import android.os.Bundle
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.models.FAQItem
import com.simplemobiletools.commons.models.Release
import com.simplemobiletools.thankyou.BuildConfig
import com.simplemobiletools.thankyou.R
import com.simplemobiletools.thankyou.databinding.ActivityMainBinding

class MainActivity : SimpleActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        appLaunched(BuildConfig.APPLICATION_ID)
        refreshMenuItems()
        setupOptionsMenu()
        checkWhatsNewDialog()
        updateMaterialActivityViews(binding.mainCoordinator, binding.activityMain, useTransparentNavigation = true, useTopSearchMenu = false)
    }

    override fun onResume() {
        super.onResume()
        updateTextColors(binding.activityMain)
        setupToolbar(binding.mainToolbar, statusBarColor = getProperBackgroundColor())
    }

    private fun refreshMenuItems() {
        binding.mainToolbar.menu.apply {
            findItem(R.id.more_apps_from_us).isVisible = !resources.getBoolean(R.bool.hide_google_relations)
        }
    }

    private fun setupOptionsMenu() {
        binding.mainToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.more_apps_from_us -> launchMoreAppsFromUsIntent()
                R.id.settings -> launchSettings()
                R.id.about -> launchAbout()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun launchSettings() {
        hideKeyboard()
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun launchAbout() {
        val faqItems = ArrayList<FAQItem>()

        if (!resources.getBoolean(R.bool.hide_google_relations)) {
            faqItems.add(FAQItem(R.string.faq_2_title_commons, R.string.faq_2_text_commons))
            faqItems.add(FAQItem(R.string.faq_6_title_commons, R.string.faq_6_text_commons))
        }

        startAboutActivity(R.string.app_name, 0, BuildConfig.VERSION_NAME, faqItems, false)
    }

    private fun checkWhatsNewDialog() {
        arrayListOf<Release>().apply {
            add(Release(14, R.string.release_14))
            add(Release(3, R.string.release_3))
            checkWhatsNew(this, BuildConfig.VERSION_CODE)
        }
    }
}
