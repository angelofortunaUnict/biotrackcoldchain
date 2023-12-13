package com.angfort.biotrakcoldchain.ui.bottomsheet

import android.content.DialogInterface
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.angfort.biotrakcoldchain.R
import com.angfort.biotrakcoldchain.databinding.ReadyToScanBsBinding
import com.angfort.biotrakcoldchain.manager.ILoaderManager
import com.angfort.biotrakcoldchain.manager.LoaderManager
import com.angfort.biotrakcoldchain.manager.NFCManager.disableFunction
import com.angfort.biotrakcoldchain.manager.NFCManager.enableFunction
import com.angfort.biotrakcoldchain.manager.NfcEnumType
import com.angfort.biotrakcoldchain.manager.ThreadUtils.runOnUiThread
import com.angfort.biotrakcoldchain.manager.Utils
import com.angfort.biotrakcoldchain.manager.click
import com.angfort.biotrakcoldchain.manager.html
import com.angfort.biotrakcoldchain.manager.nfc.ITemperatureTagHandler
import com.angfort.biotrakcoldchain.manager.nfc.TemperatureTagHandler
import com.angfort.biotrakcoldchain.ui.bottomsheet.ReadyToScanBsUtils.getBsState
import com.angfort.biotrakcoldchain.ui.main.home.HomeFragment
import com.angfort.biotrakcoldchain.ui.main.home.HomeFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unict.caennfcsdk.NfcTagInfo
import com.unict.caennfcsdk.TemperatureTag
import com.unict.caennfcsdk.gdata

class ReadyToScanBs(private val type: NfcEnumType = NfcEnumType.READ) :
    BottomSheetDialogFragment(),
    NfcAdapter.ReaderCallback,
    ILoaderManager by LoaderManager(),
    ITemperatureTagHandler by TemperatureTagHandler() {
    private var _binding: ReadyToScanBsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ReadyToScanBsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        disableFunction()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enableFunction(this)
        val (description, title, animation, strokeColor, cancelButtonTextColor) = type.getBsState()

        binding.apply {
            cancelButton.click { dismiss() }

            readyToScanDescription.html(description)
            readyToScanTitle.setTextColor(resources.getColor(title, null))
            readyToScanAnimationView.setAnimation(animation)

            cancelButton.apply {
                setStrokeColorResource(strokeColor)
                setTextColor(resources.getColor(cancelButtonTextColor, null))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onTagDiscovered(tag: Tag?) {
        try {
            tag?.let {
                val nfcTagInfo = NfcTagInfo(it)
                gdata.instance().nfcTemperature = TemperatureTag(nfcTagInfo.tag())

            }

            runOnUiThread {
                when (type) {
                    NfcEnumType.ACTIVATE -> activateTag()
                    else -> goToTagDetail()
                }
            }
        } catch (e: Exception) {
            disableFunction()
        }
    }

    private fun goToTagDetail() {
        val parentFragment = requireParentFragment() as? HomeFragment
        parentFragment?.findNavController()?.navigate(HomeFragmentDirections.actionNavigationHomeToNfcResultFragment())
        Utils.delay(2000L) {
            dismissLoader(requireContext())
        }
    }

    private fun activateTag() {
        binding.apply {
            gdata.instance().getTagSettings(true).controlRegister().samplesLoggingEnabled()
            val isLoggingEnabled = getIsSamplingEnabled()

            if (isLoggingEnabled) {
                readyToScanDescription.html(R.string.readyToScanAlreadyActive)
                disableButton.apply {
                    isVisible = true
                    setOnClickListener {
                        updateSamplingEnabled(false)
                        readyToScanDescription.html(R.string.readyToScanDisableSuccess)
                        disableButton.isVisible = false
                    }
                }
            } else {
                reset()
                Utils.delay(1000) {
                    updateSamplingEnabled(true)
                    readyToScanDescription.html(R.string.readyToScanActivationSuccess)
                    disableButton.isVisible = true
                }
            }
        }
    }
}