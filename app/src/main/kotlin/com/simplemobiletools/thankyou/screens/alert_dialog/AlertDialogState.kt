package com.simplemobiletools.thankyou.screens.alert_dialog

import androidx.compose.runtime.*

@Composable
fun rememberAlertDialogState(
    isShownInitially: Boolean = false
) = remember { AlertDialogState(isShownInitially) }

@Stable
class AlertDialogState(isShownInitially: Boolean = false) {
    var isShown by mutableStateOf(isShownInitially)
        private set

    fun show() {
        isShown = true
    }

    fun hide() {
        isShown = false
    }

    fun toggle() {
        isShown = !isShown
    }

    fun changeValue(predicate: Boolean) {
        isShown = predicate
    }

    @Composable
    fun DialogMember(
        content: @Composable () -> Unit
    ) {
        if (isShown) {
            content()
        }
    }
}
