package com.angfort.biotrakcoldchain.manager.nfc

import android.content.Context
import android.graphics.Color
import android.nfc.NfcAdapter
import android.util.Log
import androidx.core.content.ContextCompat
import com.angfort.biotrakcoldchain.R
import com.angfort.biotrakcoldchain.manager.BaseFunction
import com.angfort.biotrakcoldchain.manager.PrefEnum
import com.angfort.biotrakcoldchain.manager.SecurityManager.getFromShared
import com.angfort.biotrakcoldchain.manager.logPrint
import com.angfort.biotrakcoldchain.ui.main.nfcresult.TemperatureNfcMarker
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.unict.caennfcsdk.SensorSample
import com.unict.caennfcsdk.gdata
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

interface ITemperatureTagHandler {
    fun setSamples(action: BaseFunction)
    fun renderData(context: Context, idTemperatureLineChart: LineChart, response: (SensorSample) -> Unit)
    fun getUpSamples(): ArrayList<Pair<String, String>>
    fun getDownSamples(): ArrayList<Pair<String, String>>
    fun getMinMaxMeanValue(): Triple<Float?, Float?, Float>
    fun getIsSamplingEnabled(): Boolean
    fun updateSamplingEnabled(value: Boolean)
    fun reset()

    val dayFormat: SimpleDateFormat
    val hourFormat: SimpleDateFormat
}

class TemperatureTagHandler : ITemperatureTagHandler {
    var mDefaultAdapter: NfcAdapter? = null
    var sensorSamples = arrayOf<SensorSample>()
    val samples = ArrayList<Entry>()
    private var sample_min = 0f
    private var sample_max = 0f

    private val _lb_date_format = SimpleDateFormat("dd/MM HH:mm:ss")
    private var _x_labels: Array<String>? = null

    override val dayFormat = SimpleDateFormat("dd/MM")
    override val hourFormat = SimpleDateFormat("HH:mm:ss")

    override fun setSamples(action: BaseFunction) {
        val nfcTemperature = gdata.instance().nfcTemperature
        nfcTemperature?.let {
            val outputRegisters = it.outputRegisters
            Log.d("CNDEBUG", "[Samples Num] ${outputRegisters.getSamplesNumber()}".trimIndent())
            sensorSamples = nfcTemperature.getAllSamples(outputRegisters)

            sample_min = 85.0f
            sample_max = -40.0f

            val arrayList = arrayListOf<String>()

            sensorSamples.forEachIndexed { index, sample ->
                sample.timestamp().time.logPrint("timestampLong")
                val value = sample.value()
                if (value < 99.0f) {
                    if (value > sample_max) {
                        sample_max = value
                    }
                    if (value < sample_min) {
                        sample_min = value
                    }

                    samples.add(
                        Entry(
                            index.toFloat(), value
                        )
                    )
                    arrayList.add(_lb_date_format.format(sample.timestamp()))
                }

                "${_lb_date_format.format(sample.timestamp())} ~ $value".logPrint("samples")
            }

            _x_labels = arrayList.toTypedArray()

            action()
        }
    }

    override fun getUpSamples(): ArrayList<Pair<String, String>> {
        val upSamples = sensorSamples.filter {
            val value = it.value()
            val maxThreshold = getFromShared<String>(PrefEnum.THRESHOLD_MAX)?.toFloatOrNull() ?: 0f
            value > maxThreshold
        }

        val upSampleList = upSamples.map {
            Pair<String, String>(_lb_date_format.format(it.timestamp()), it.value().toString())
        }

        return ArrayList(upSampleList)
    }

    override fun getMinMaxMeanValue(): Triple<Float?, Float?, Float> {
        var min: Float? = null
        var max: Float? = null
        var sum = 0f
        var sumNum = 0

        sensorSamples.forEach { sensorSample ->
            val value = sensorSample.value()
            max?.let {
                if (value > it) max = value
            } ?: run {
                max = value
            }

            min?.let {
                if (value < it) min = value
            } ?: run {
                min = value
            }

            sum += value
            sumNum += 1
        }

        val mean = sum / sumNum

        return Triple(min, max, mean)
    }

    override fun getIsSamplingEnabled(): Boolean {
        return try {
            val value = gdata.instance().getTagSettings(true)
            value.controlRegister().samplesLoggingEnabled()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun updateSamplingEnabled(value: Boolean) {
        try {
            gdata.instance().updateEnableLogging(value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun reset() {
        try {
            gdata.instance().reset()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getDownSamples(): ArrayList<Pair<String, String>> {
        val upSamples = sensorSamples.filter {
            val value = it.value()
            val minThreshold = getFromShared<String>(PrefEnum.THRESHOLD_MIN)?.toFloatOrNull() ?: 0f
            value < minThreshold
        }

        val upSampleList = upSamples.map {
            Pair<String, String>(_lb_date_format.format(it.timestamp()), it.value().toString())
        }

        return ArrayList(upSampleList)
    }

    override fun renderData(context: Context, idTemperatureLineChart: LineChart, response: (SensorSample) -> Unit) {
        idTemperatureLineChart.legend.textColor = ContextCompat.getColor(context, R.color.white)

        val list = samples
        val lineDataSet = LineDataSet(list, "Campioni di temperatura")

        lineDataSet.color = ContextCompat.getColor(context, R.color.green)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircleHole(true)
        lineDataSet.setCircleColor(ContextCompat.getColor(context, R.color.yellow))
        lineDataSet.valueTextColor = ContextCompat.getColor(context, R.color.white)
        lineDataSet.setDrawFilled(true)

        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val xAxis: XAxis = idTemperatureLineChart.xAxis
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 0.1f
            labelRotationAngle = -45.0f
            axisLineColor = ContextCompat.getColor(context, R.color.white)
            gridColor = ContextCompat.getColor(context, R.color.white)
            textColor = ContextCompat.getColor(context, R.color.white)
            valueFormatter = IndexAxisValueFormatter(_x_labels)
            textColor = ContextCompat.getColor(context, R.color.white)
        }

        val axisLeft: YAxis = idTemperatureLineChart.axisLeft
        axisLeft.apply {
            granularity = 0.1f
            axisMinimum = sample_min - 1.0f
            axisMaximum = sample_max + 1.0f
            axisLineColor = ContextCompat.getColor(context, R.color.white)
            gridColor = ContextCompat.getColor(context, R.color.white)
            textColor = ContextCompat.getColor(context, R.color.white)
            idTemperatureLineChart.data = LineData(lineDataSet)
            idTemperatureLineChart.invalidate()

            val maxThreshold = getFromShared<String>(PrefEnum.THRESHOLD_MAX)?.toFloatOrNull() ?: 0f
            val minThreshold = getFromShared<String>(PrefEnum.THRESHOLD_MIN)?.toFloatOrNull() ?: 0f

            val limitLineMax = LimitLine(maxThreshold, "max")
            limitLineMax.lineWidth = 2f // imposta la larghezza della linea a 1 pixel
            limitLineMax.lineColor = Color.RED // imposta il colore della linea a rosso
            limitLineMax.enableDashedLine(40f, 20f, 10f) // imposta il tratteggio della linea a 10 pixel on, 10 pixel off
            limitLineMax.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP // posizione della label

            val limitLineMin = LimitLine(minThreshold, "min")
            limitLineMin.lineWidth = 3f // imposta la larghezza della linea a 1 pixel
            limitLineMin.lineColor = Color.CYAN // imposta il colore della linea a rosso
            limitLineMin.enableDashedLine(40f, 20f, 10f) // imposta il tratteggio della linea a 10 pixel on, 10 pixel off
            limitLineMin.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP // posizione della label

            addLimitLine(limitLineMax)
            addLimitLine(limitLineMin)
        }

        val marker = TemperatureNfcMarker(context = context, layoutResource = R.layout.custom_marker_view) {
            val (x, y) = it
            response(sensorSamples[x?.roundToInt() ?: 0])
        }

        idTemperatureLineChart.marker = marker

        idTemperatureLineChart.apply {
            setTouchEnabled(true)
            setPinchZoom(true)
        }

        val dataSet = idTemperatureLineChart.data.getDataSetByIndex(0) as LineDataSet
        val lastIndex = dataSet.entryCount
        val entry = dataSet.getEntryForIndex(lastIndex - 1)
        val highlightIndex = dataSet.getEntryIndex(entry).toFloat()

        idTemperatureLineChart.highlightValue(highlightIndex, 0)
        idTemperatureLineChart.invalidate()
    }
}