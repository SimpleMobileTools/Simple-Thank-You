package com.simplemobiletools.thankyou.extensions

import android.content.Context
import com.simplemobiletools.thankyou.helpers.Config

val Context.config: Config get() = Config.newInstance(this)
