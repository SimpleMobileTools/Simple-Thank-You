package com.simplemobiletools.thankyou.activities

import android.content.Intent
import android.os.Bundle
import com.simplemobiletools.commons.extensions.appLaunched
import com.simplemobiletools.commons.extensions.checkWhatsNew
import com.simplemobiletools.commons.extensions.hideKeyboard
import com.simplemobiletools.commons.extensions.updateTextColors
import com.simplemobiletools.commons.models.FAQItem
import com.simplemobiletools.commons.models.Release
import com.simplemobiletools.thankyou.BuildConfig
import com.simplemobiletools.thankyou.R
import com.simplemobiletools.thankyou.databinding.ActivityMainBinding

class MainActivity : SimpleActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appLaunched(BuildConfig.APPLICATION_ID)
        setupOptionsMenu()
        checkWhatsNewDialog()
    }

    override fun onResume() {
        super.onResume()
        updateTextColors(binding.activityMain)
        setupToolbar(binding.mainToolbar)
    }

    private fun setupOptionsMenu() {
        binding.mainToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
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
