package com.simplemobiletools.thankyou.activities

import android.os.Bundle
import com.simplemobiletools.thankyou.R

class MainActivity : SimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onDestroy() {
        super.onDestroy()
        mConfig.isFirstRun = false
    }
}
