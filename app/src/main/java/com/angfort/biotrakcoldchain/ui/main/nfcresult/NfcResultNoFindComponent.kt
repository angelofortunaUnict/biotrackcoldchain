package com.angfort.biotrakcoldchain.ui.main.nfcresult

import com.angfort.biotrakcoldchain.R
import com.angfort.biotrakcoldchain.databinding.ResultNfcNotCorrectItemBinding
import com.angfort.biotrakcoldchain.manager.getString
import com.angfort.biotrakcoldchain.manager.roundToFirstDecimal

data class NfcResultNoFindComponentModel(
    val isUpMax: Boolean, val date: String, val temp: String
)

fun ResultNfcNotCorrectItemBinding.initNoCorrectItem(model: NfcResultNoFindComponentModel) {
    with(model) {
        thermIcon.setImageResource(if (isUpMax) R.drawable.ic_baseline_thermostat_24_red else R.drawable.ic_baseline_thermostat_24_blu)
        resultNfcNotCorrectDate.text = date
        resultNfcNotCorrectTemp.text = String.format(root.getString(R.string.temp_str), temp.toFloatOrNull()?.roundToFirstDecimal().toString())
    }
}
