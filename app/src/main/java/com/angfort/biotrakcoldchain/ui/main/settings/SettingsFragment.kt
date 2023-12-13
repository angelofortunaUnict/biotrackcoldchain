package com.angfort.biotrakcoldchain.ui.main.settings

import com.angfort.biotrakcoldchain.databinding.FragmentSettingsBinding
import com.angfort.biotrakcoldchain.manager.*
import com.angfort.biotrakcoldchain.manager.SecurityManager.getFromShared
import com.angfort.biotrakcoldchain.manager.SecurityManager.putIntoShared
import com.angfort.biotrakcoldchain.ui.base.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override fun setInflater() = FragmentSettingsBinding.inflate(layoutInflater)

    override fun FragmentSettingsBinding.setUI() {
        settingsToolbar.setNavigationOnClickListener {
            goBack()
        }

        settingsLl.hideKeyboardOnClick()

        etThresholdMaxSettings.apply {
            val maxThreshold = getFromShared<String>(PrefEnum.THRESHOLD_MAX)
            text(maxThreshold ?: "-")
            doAfterTextChanged {
                it?.let {
                    putIntoShared(PrefEnum.THRESHOLD_MAX to it)
                }
            }
        }

        etThresholdMinSettings.apply {
            val minThreshold = getFromShared<String>(PrefEnum.THRESHOLD_MIN)
            text(minThreshold ?: "-")
            doAfterTextChanged {
                it?.let {
                    putIntoShared(PrefEnum.THRESHOLD_MIN to it)
                }
            }
        }
    }
}
