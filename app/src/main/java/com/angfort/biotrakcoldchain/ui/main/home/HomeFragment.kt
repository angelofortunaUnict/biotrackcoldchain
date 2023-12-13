package com.angfort.biotrakcoldchain.ui.main.home

import androidx.navigation.fragment.findNavController
import com.angfort.biotrakcoldchain.R
import com.angfort.biotrakcoldchain.databinding.FragmentHomeBinding
import com.angfort.biotrakcoldchain.manager.NFCManager.bs
import com.angfort.biotrakcoldchain.manager.NfcEnumType
import com.angfort.biotrakcoldchain.manager.click
import com.angfort.biotrakcoldchain.ui.base.BaseFragment
import com.angfort.biotrakcoldchain.ui.bottomsheet.ReadyToScanBs

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun setInflater() = FragmentHomeBinding.inflate(layoutInflater)

    override fun FragmentHomeBinding.setUI() {
        enableTemperatureTag.click {
            bs = ReadyToScanBs(type = NfcEnumType.ACTIVATE)
            bs?.show(childFragmentManager, "activate_bs")
        }

        readNfcTag.setOnClickListener {
            bs = ReadyToScanBs(type = NfcEnumType.READ)
            bs?.show(childFragmentManager, "read_bs")
        }

        homeToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.homeMenuSettings) {
                findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToSettingsFragment())
                true
            } else false
        }
    }
}
