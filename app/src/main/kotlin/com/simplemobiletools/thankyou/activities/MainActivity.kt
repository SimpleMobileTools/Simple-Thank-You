package com.simplemobiletools.thankyou.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.simplemobiletools.commons.compose.alert_dialog.AlertDialogState
import com.simplemobiletools.commons.compose.alert_dialog.rememberAlertDialogState
import com.simplemobiletools.commons.compose.extensions.enableEdgeToEdgeSimple
import com.simplemobiletools.commons.compose.extensions.linkColor
import com.simplemobiletools.commons.compose.extensions.onEventValue
import com.simplemobiletools.commons.compose.theme.AppThemeSurface
import com.simplemobiletools.commons.dialogs.DonateAlertDialog
import com.simplemobiletools.commons.dialogs.RateStarsAlertDialog
import com.simplemobiletools.commons.dialogs.UpgradeToProAlertDialog
import com.simplemobiletools.commons.dialogs.WhatsNewAlertDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.models.FAQItem
import com.simplemobiletools.commons.models.Release
import com.simplemobiletools.thankyou.BuildConfig
import com.simplemobiletools.thankyou.R
import com.simplemobiletools.thankyou.extensions.startAboutActivity
import com.simplemobiletools.thankyou.helpers.appLaunchedCompose
import com.simplemobiletools.thankyou.helpers.checkWhatsNewCompose
import com.simplemobiletools.thankyou.helpers.upgradeToPro
import com.simplemobiletools.thankyou.screens.MainScreen
import kotlinx.collections.immutable.toImmutableList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()
        setContent {
            AppThemeSurface {
                val releasesList = remember { mutableStateListOf<Release>() }
                val checkWhatsNewAlertDialogState = getCheckWhatsNewAlertDialogState(releasesList)
                val linkColor = linkColor()
                val showMoreApps = onEventValue { !resources.getBoolean(R.bool.hide_google_relations) }
                MainScreen(
                    linkColor = linkColor,
                    showMoreApps = showMoreApps,
                    openSettings = ::launchSettings,
                    openAbout = ::launchAbout,
                    moreAppsFromUs = ::launchMoreAppsFromUsIntent
                )
                AppLaunched()
                CheckWhatsNew(releasesList, checkWhatsNewAlertDialogState)
            }
        }
    }

    @Composable
    private fun AppLaunched(
        donateAlertDialogState: AlertDialogState = getDonateAlertDialogState(),
        rateStarsAlertDialogState: AlertDialogState = getRateStarsAlertDialogState(),
        upgradeToProAlertDialogState: AlertDialogState = getUpgradeToProAlertDialogState()
    ) {
        LaunchedEffect(Unit) {
            appLaunchedCompose(
                appId = BuildConfig.APPLICATION_ID,
                showDonateDialog = donateAlertDialogState::show,
                showRateUsDialog = rateStarsAlertDialogState::show,
                showUpgradeDialog = upgradeToProAlertDialogState::show
            )
        }
    }

    @Composable
    private fun CheckWhatsNew(
        releasesList: SnapshotStateList<Release>,
        checkWhatsNewAlertDialogState: AlertDialogState
    ) {
        DisposableEffect(Unit) {
            checkWhatsNewCompose(
                releases = listOf(
                    Release(14, R.string.release_14),
                    Release(3, R.string.release_3)
                ),
                currVersion = BuildConfig.VERSION_CODE,
                showWhatsNewDialog = { releases ->
                    releasesList.addAll(releases)
                    checkWhatsNewAlertDialogState.show()
                }
            )
            onDispose {
                releasesList.clear()
            }
        }
    }

    @Composable
    private fun getUpgradeToProAlertDialogState() = rememberAlertDialogState().apply {
        DialogMember {
            UpgradeToProAlertDialog(
                alertDialogState = this, onMoreInfoClick = ::upgradeToPro, onUpgradeClick = ::launchUpgradeToProIntent
            )
        }
    }


    @Composable
    private fun getCheckWhatsNewAlertDialogState(releasesList: SnapshotStateList<Release>) = rememberAlertDialogState().apply {
        DialogMember {
            WhatsNewAlertDialog(alertDialogState = this, releases = releasesList.toImmutableList())
        }
    }

    @Composable
    private fun getDonateAlertDialogState() =
        rememberAlertDialogState().apply {
            DialogMember {
                DonateAlertDialog(alertDialogState = this)
            }
        }

    @Composable
    private fun getRateStarsAlertDialogState() = rememberAlertDialogState().apply {
        DialogMember {
            RateStarsAlertDialog(alertDialogState = this) { stars ->
                if (stars == 5) {
                    redirectToRateUs()
                }
                toast(com.simplemobiletools.commons.R.string.thank_you)
                baseConfig.wasAppRated = true
            }
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
}
