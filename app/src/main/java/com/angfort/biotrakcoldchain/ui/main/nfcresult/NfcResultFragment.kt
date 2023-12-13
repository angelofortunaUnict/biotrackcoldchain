package com.angfort.biotrakcoldchain.ui.main.nfcresult

import android.view.LayoutInflater
import android.view.ViewGroup
import com.angfort.biotrakcoldchain.R
import com.angfort.biotrakcoldchain.databinding.FragmentNfcResultBinding
import com.angfort.biotrakcoldchain.databinding.ResultNfcNotCorrectItemBinding
import com.angfort.biotrakcoldchain.manager.*
import com.angfort.biotrakcoldchain.manager.SecurityManager.getFromShared
import com.angfort.biotrakcoldchain.manager.nfc.ITemperatureTagHandler
import com.angfort.biotrakcoldchain.manager.nfc.TemperatureTagHandler
import com.angfort.biotrakcoldchain.ui.base.BaseAdapter
import com.angfort.biotrakcoldchain.ui.base.BaseFragment

class NfcResultFragment : BaseFragment<FragmentNfcResultBinding>(), ITemperatureTagHandler by TemperatureTagHandler() {
    override fun setInflater() = FragmentNfcResultBinding.inflate(layoutInflater)

    private val adapter: BaseAdapter<NfcResultNoFindComponentModel, ResultNfcNotCorrectItemBinding> by lazy {
        object : BaseAdapter<NfcResultNoFindComponentModel, ResultNfcNotCorrectItemBinding>(requireContext()) {
            override fun setBindingClass(inflater: LayoutInflater, p0: ViewGroup) = ResultNfcNotCorrectItemBinding.inflate(inflater, p0, false)
            override fun bindItem(item: NfcResultNoFindComponentModel, position: Int) = binding.initNoCorrectItem(item)
        }
    }

    override fun FragmentNfcResultBinding.setUI() {
        try {
            materialToolbar.setNavigationOnClickListener { goBack() }

            setSamples {
                renderData(requireContext(), idTemperatureLineChart) {
                    val x = it.timestamp()
                    val y = it.value()

                    val date = dayFormat.format(x)
                    val hour = hourFormat.format(x)

                    selectedValueYChart.text = String.format(getString(R.string.temp_str), y.roundToFirstDecimal())
                    selectedValueYChart.visible()

                    selectedValueDay.text = "$date - $hour"
                }

                val upSamples = getUpSamples()
                val downSamples = getDownSamples()

                setInfoCard(upSamples, downSamples)

                val (min, max, mean) = getMinMaxMeanValue()

                setMaxTemp(max)
                setMinTemp(min)
                setMeanTemp(mean)

                setRecyclerView(upSamples, downSamples)
            }
        } catch (e: Exception) {
            contentNsv.gone()
            notFoundContainer.visible()
            materialToolbar.setBackgroundColor(requireContext().getColor(R.color.white))
            materialToolbar.setNavigationIconTint(requireContext().getColor(R.color.black))
        }
    }

    private fun FragmentNfcResultBinding.setRecyclerView(
        upSamples: ArrayList<Pair<String, String>>,
        downSamples: ArrayList<Pair<String, String>>
    ) {
        selectedValueNotCorrectRv.vertical(adapter)
        selectedValueNotCorrectRv.setHasFixedSize(true)
        if (upSamples.size > 0) {
            val convertedUp = upSamples.map {
                val (dateValue, temp) = it
                NfcResultNoFindComponentModel(true, dateValue, temp)
            }
            adapter.addItemList(ArrayList(convertedUp))
        }

        if (downSamples.size > 0) {
            val convertedDown = downSamples.map {
                val (dateValue, temp) = it
                NfcResultNoFindComponentModel(false, dateValue, temp)
            }
            adapter.addItemList(ArrayList(convertedDown))
        }
    }

    private fun FragmentNfcResultBinding.setInfoCard(
        upSamples: ArrayList<Pair<String, String>>,
        downSamples: ArrayList<Pair<String, String>>
    ) {
        when {
            upSamples.size == 0 && downSamples.size == 0 -> {
                nfcResultCardIcon.setImageResource(R.drawable.ic_baseline_check_circle_24)
                selectedValueCard.setCardBackgroundColor(resources.getColor(R.color.green, null))
                nfcResultCardDescription.text = getString(R.string.correct_result_temp)
                nfcResultAnomalies.gone()
                selectedValueNotCorrectRv.gone()
            }
            else -> {
                selectedValueCard.setCardBackgroundColor(resources.getColor(R.color.orange, null))
                nfcResultCardIcon.setImageResource(R.drawable.ic_baseline_warning_24)
                nfcResultCardDescription.text = getString(R.string.no_correct_result_temp)
                nfcResultAnomalies.visible()
                selectedValueNotCorrectRv.visible()
            }
        }
    }

    private fun FragmentNfcResultBinding.setMeanTemp(mean: Float) {
        selectedValueMeanTemp.apply {
            text = String.format(getString(R.string.temp_str), mean.roundToFirstDecimal())
        }
    }

    private fun FragmentNfcResultBinding.setMinTemp(min: Float?) {
        selectedValueMinTemp.apply {
            text = String.format(getString(R.string.temp_str), min?.roundToFirstDecimal())
            setTextColor(
                if ((min ?: 0f) < (getFromShared<String>(PrefEnum.THRESHOLD_MIN)?.toFloatOrNull() ?: 0f)) resources.getColor(R.color.blueLight, null)
                else resources.getColor(R.color.green, null)
            )
        }
    }

    private fun FragmentNfcResultBinding.setMaxTemp(max: Float?) {
        selectedValueMaxTemp.apply {
            text = String.format(getString(R.string.temp_str), max?.roundToFirstDecimal())
            setTextColor(
                if ((max ?: 0f) > (getFromShared<String>(PrefEnum.THRESHOLD_MAX)?.toFloatOrNull() ?: 0f)) resources.getColor(R.color.orange, null)
                else resources.getColor(R.color.green, null)
            )
        }
    }
}
