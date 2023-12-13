package com.angfort.biotrakcoldchain.ui.main.nfcresult

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import com.angfort.biotrakcoldchain.R
import com.angfort.biotrakcoldchain.manager.roundToFirstDecimal
import com.angfort.biotrakcoldchain.manager.visible
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class TemperatureNfcMarker(context: Context, val layoutResource: Int, val response: (Pair<Double?, Double?>) -> Unit) : MarkerView(context, layoutResource) {
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)

        val markerText = findViewById<TextView>(R.id.marker_text)
        val markerLayout = findViewById<FrameLayout>(R.id.marker_layout)

        val value = e?.y?.toDouble()

        if (value != null) {
            markerText.text = context.getString(R.string.param_celsius, value.roundToFirstDecimal().toString())
            markerLayout.visible()

            response(e.x.toDouble() to e.y.toDouble())
        }
    }

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        return MPPointF(-width / 2f, -height / 2f)
    }
}