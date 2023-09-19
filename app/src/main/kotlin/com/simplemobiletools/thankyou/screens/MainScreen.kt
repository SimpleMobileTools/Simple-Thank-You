@file:OptIn(ExperimentalMaterial3Api::class)

package com.simplemobiletools.thankyou.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simplemobiletools.commons.R
import com.simplemobiletools.commons.compose.extensions.MyDevices
import com.simplemobiletools.commons.compose.menus.ActionItem
import com.simplemobiletools.commons.compose.menus.ActionMenu
import com.simplemobiletools.commons.compose.menus.OverflowMode
import com.simplemobiletools.commons.compose.screens.LinkifyText
import com.simplemobiletools.commons.compose.screens.stringFromHTML
import com.simplemobiletools.commons.compose.settings.scaffold.*
import com.simplemobiletools.commons.compose.theme.AppThemeSurface
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun MainScreen(
    showMoreApps: Boolean,
    openSettings: () -> Unit,
    openAbout: () -> Unit,
    moreAppsFromUs: () -> Unit,
) {

    SettingsLazyScaffold(customTopBar = { scrolledColor: Color, _: MutableInteractionSource, scrollBehavior: TopAppBarScrollBehavior, statusBarColor: Int, colorTransitionFraction: Float, contrastColor: Color ->
        TopAppBar(
            title = {},
            actions = {
                val actionMenus = remember {
                    val list = listOf(
                        ActionItem(R.string.settings, icon = Icons.Outlined.Settings, doAction = openSettings, overflowMode = OverflowMode.NEVER_OVERFLOW),
                        ActionItem(R.string.about, icon = Icons.Outlined.Info, doAction = openAbout, overflowMode = OverflowMode.NEVER_OVERFLOW),
                    )
                    if (showMoreApps) {
                        list + ActionItem(R.string.more_apps_from_us, doAction = moreAppsFromUs, overflowMode = OverflowMode.ALWAYS_OVERFLOW)
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
    }) {
        val source = stringResource(id = com.simplemobiletools.thankyou.R.string.main_text)
        LinkifyText(modifier = Modifier.padding(40.dp), fontSize = 16.sp) {
            stringFromHTML(source)
        }
    }
}

@Composable
fun topAppBarColors(
    statusBarColor: Int,
    colorTransitionFraction: Float,
    contrastColor: Color
) = TopAppBarDefaults.topAppBarColors(
    scrolledContainerColor = Color(statusBarColor),
    containerColor = if (colorTransitionFraction == 1f) contrastColor else MaterialTheme.colorScheme.surface,
    navigationIconContentColor = if (colorTransitionFraction == 1f) contrastColor else MaterialTheme.colorScheme.surface
)

@Composable
private fun ActionMenuMainScreen(
) {
    var isMenuVisible by remember { mutableStateOf(false) }
    val actionMenus: ImmutableList<ActionItem> = remember {
        listOf(
            ActionItem(nameRes = R.string.settings, icon = Icons.Default.Settings, overflowMode = OverflowMode.NEVER_OVERFLOW, doAction = {}),
            ActionItem(nameRes = R.string.about, icon = Icons.Default.Info, overflowMode = OverflowMode.NEVER_OVERFLOW, doAction = {}),
            ActionItem(nameRes = R.string.more_apps_from_us, icon = null, overflowMode = OverflowMode.ALWAYS_OVERFLOW, doAction = {}),
        ).toImmutableList()
    }
    ActionMenu(items = actionMenus, numIcons = 2, isMenuVisible = isMenuVisible, onMenuToggle = { isMenuVisible = !isMenuVisible })
}


@Composable
@MyDevices
private fun MainScreenPreview() {
    AppThemeSurface {
        MainScreen(showMoreApps = true, openSettings = {}, openAbout = {}, moreAppsFromUs = {})
    }
}
