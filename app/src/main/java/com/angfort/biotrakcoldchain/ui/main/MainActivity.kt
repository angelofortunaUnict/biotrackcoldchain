package com.angfort.biotrakcoldchain.ui.main

import com.angfort.biotrakcoldchain.databinding.ActivityMainBinding
import com.angfort.biotrakcoldchain.manager.Config
import com.angfort.biotrakcoldchain.manager.PrefEnum
import com.angfort.biotrakcoldchain.manager.SecurityManager.containsKey
import com.angfort.biotrakcoldchain.manager.SecurityManager.getFromShared
import com.angfort.biotrakcoldchain.manager.SecurityManager.initSharedPref
import com.angfort.biotrakcoldchain.manager.SecurityManager.putIntoShared
import com.angfort.biotrakcoldchain.ui.base.BaseActivity
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.zebra.rfid.api3.RFIDReader

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun setInflater() = ActivityMainBinding.inflate(layoutInflater)

    override fun ActivityMainBinding.setUI() {
        setLogger()
        initSharedPref(this@MainActivity, Config.PREF_NAME)
        checkSharedPref()
    }

    private fun setLogger() {
        Logger.addLogAdapter(object : AndroidLogAdapter() {})

    }

    private fun checkSharedPref() {
        if (getFromShared<String>(PrefEnum.THRESHOLD_MAX) == null) {
            putIntoShared(PrefEnum.THRESHOLD_MAX to "26.0")
        }

        if (getFromShared<String>(PrefEnum.THRESHOLD_MIN) == null) {
            putIntoShared(PrefEnum.THRESHOLD_MIN to "23.0")
        }
    }
}
