package com.angfort.biotrakcoldchain.ui.bottomsheet

import androidx.annotation.ColorRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.angfort.biotrakcoldchain.R
import com.angfort.biotrakcoldchain.manager.NfcEnumType

data class ReadyToScanBsState(
    @StringRes val description: Int,
    @StringRes val title: Int,
    @RawRes val animation: Int,
    @ColorRes val strokeColor: Int,
    @ColorRes val cancelButtonTextColor: Int
)

object ReadyToScanBsUtils {
    private fun NfcEnumType.getReadyToScanDescription() = when (this) {
        NfcEnumType.READ -> R.string.readyToScanMessage
        NfcEnumType.WRITE -> R.string.readyToScanWriteMessage
        NfcEnumType.ACTIVATE -> R.string.readyToScanActiveMessage
    }

    private fun NfcEnumType.getReadyToScanTitle() = when (this) {
        NfcEnumType.READ -> R.color.blueLight
        else -> R.color.red
    }

    private fun NfcEnumType.getAnimation() = when (this) {
        NfcEnumType.READ -> R.raw.nfc_reader_read
        else -> R.raw.nfc_reader_write
    }

    private fun NfcEnumType.getStrokeColor() = when (this) {
        NfcEnumType.READ -> R.color.blueLight
        else -> R.color.red
    }

    private fun NfcEnumType.getCancelButtonTextColor() = when (this) {
        NfcEnumType.READ -> R.color.blueLight
        else -> R.color.red
    }

    fun NfcEnumType.getBsState() = ReadyToScanBsState(
        description = getReadyToScanDescription(),
        title = getReadyToScanTitle(),
        animation = getAnimation(),
        strokeColor = getStrokeColor(),
        cancelButtonTextColor = getCancelButtonTextColor()
    )
}
