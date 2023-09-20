@file:OptIn(ExperimentalMaterial3Api::class)

package com.simplemobiletools.thankyou.screens

import android.text.util.Linkify
import android.view.Gravity
import android.widget.TextView
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.simplemobiletools.commons.R
import com.simplemobiletools.commons.compose.extensions.MyDevices
import com.simplemobiletools.commons.compose.menus.ActionItem
import com.simplemobiletools.commons.compose.menus.ActionMenu
import com.simplemobiletools.commons.compose.menus.OverflowMode
import com.simplemobiletools.commons.compose.settings.scaffold.*
import com.simplemobiletools.commons.compose.theme.AppThemeSurface
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun MainScreen(
    showMoreApps: Boolean,
    openSettings: () -> Unit,
    openAbout: () -> Unit,
    moreAppsFromUs: () -> Unit,
    linkColor: Int,
) {
    SettingsLazyScaffold(customTopBar = { scrolledColor: Color, _: MutableInteractionSource, scrollBehavior: TopAppBarScrollBehavior, statusBarColor: Int, colorTransitionFraction: Float, contrastColor: Color ->
        TopAppBar(
            title = {},
            actions = {
                val actionMenus = remember {
                    val settings =
                        ActionItem(R.string.settings, icon = Icons.Filled.Settings, doAction = openSettings, overflowMode = OverflowMode.NEVER_OVERFLOW)
                    val about = ActionItem(R.string.about, icon = Icons.Outlined.Info, doAction = openAbout, overflowMode = OverflowMode.NEVER_OVERFLOW)

                    val list = if (showMoreApps) {
                        listOf(settings, about, ActionItem(R.string.more_apps_from_us, doAction = moreAppsFromUs, overflowMode = OverflowMode.ALWAYS_OVERFLOW))
                    } else {
                        listOf(settings, about)
                    }
                    list.toImmutableList()
                }
                var isMenuVisible by remember { mutableStateOf(false) }
                ActionMenu(items = actionMenus, numIcons = 2, isMenuVisible = isMenuVisible, onMenuToggle = { isMenuVisible = it }, iconsColor = scrolledColor)
            },
            scrollBehavior = scrollBehavior,
            colors = topAppBarColors(statusBarColor, colorTransitionFraction, contrastColor),
            modifier = Modifier.topAppBarPaddings(),
            windowInsets = topAppBarInsets()
        )
    }) { paddingValues ->
        val textColor = MaterialTheme.colorScheme.onSurface.toArgb()

        AndroidView(
            factory = { context ->
                TextView(context).apply {
                    setText(com.simplemobiletools.thankyou.R.string.main_text)
                    textSize = 16.sp.value
                    setLineSpacing(3.dp.value, 1f)
                    gravity = Gravity.CENTER_HORIZONTAL
                    Linkify.addLinks(this, Linkify.WEB_URLS)
                    Linkify.addLinks(this, Linkify.EMAIL_ADDRESSES)
                }
            }, modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .padding(40.dp),
            update = { textView ->
                textView.setLinkTextColor(linkColor)
                textView.setTextColor(textColor)
            }
        )
    }
}

@Composable
@MyDevices
private fun MainScreenPreview() {
    AppThemeSurface {
        MainScreen(showMoreApps = true, openSettings = {}, openAbout = {}, moreAppsFromUs = {}, linkColor = MaterialTheme.colorScheme.onSurface.toArgb())
    }
}
