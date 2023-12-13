package com.angfort.biotrakcoldchain.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.nfc.tech.NfcA
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.annotation.ColorInt
import androidx.core.internal.view.SupportMenu
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.angfort.biotrakcoldchain.manager.ThreadUtils.runOnUiThread
import com.angfort.biotrakcoldchain.data.model.TagModel
import com.angfort.biotrakcoldchain.ui.base.BaseActivityNfc
import com.angfort.biotrakcoldchain.ui.base.BaseFragmentNfc
import com.angfort.biotrakcoldchain.ui.bottomsheet.ReadyToScanBs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.lang.Compiler.disable
import java.nio.charset.StandardCharsets
import java.util.*

enum class NfcEnumType {
    READ,
    WRITE,
    ACTIVATE
}

object NFCManager {
    private val HEX_CHAR_TABLE =
        byteArrayOf(48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70)

    private var nfcAdapter: NfcAdapter? = null
    var bs: ReadyToScanBs? = null

    fun <T : ViewBinding> BaseFragmentNfc<T>.setNfcReader(
        type: NfcEnumType = NfcEnumType.READ,
        hasBottomSheet: Boolean = true,
        tag: String = "bs"
    ): BaseFunction {
        nfcAdapter = NfcAdapter.getDefaultAdapter(requireActivity())

        return {
            enable(this)
            if (hasBottomSheet)
                this.setNfcReaderFunction(type, tag).invoke()
        }
    }

    fun <T : ViewBinding> BaseActivityNfc<T>.setNfcReader(
        type: NfcEnumType = NfcEnumType.READ,
        hasBottomSheet: Boolean = true
    ): BaseFunction {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        return {
            enable(this)
            if (hasBottomSheet)
                setNfcReaderFunction(type).invoke()
        }
    }

    private fun BaseFragmentNfc<*>.setNfcReaderFunction(type: NfcEnumType = NfcEnumType.READ, tag: String = "bs"): BaseFunction {
        return {
            bs = ReadyToScanBs(type)
            bs?.show(childFragmentManager, tag)
            bs?.onCancel(object : DialogInterface {
                override fun cancel() {
                    this@setNfcReaderFunction.disableNfc()
                    bs = null
                }

                override fun dismiss() {
                    disableNfc()
                    bs = null
                }
            })
        }
    }

    private fun BaseActivityNfc<*>.setNfcReaderFunction(type: NfcEnumType = NfcEnumType.READ): BaseFunction {
        return {
            bs = ReadyToScanBs(type)
            bs?.show(supportFragmentManager, "bs")
            bs?.onCancel(object : DialogInterface {
                override fun cancel() {
                    disable()
                    bs = null
                }

                override fun dismiss() {
                    disable()
                    bs = null
                }
            })
        }
    }

    fun <T : ViewBinding> BaseFragmentNfc<T>.dismissNfcBs() = dismissNfcBsFunction()

    private fun Fragment.dismissNfcBsFunction() {
        nfcAdapter?.disableReaderMode(requireActivity())
        bs?.dismiss()
        bs = null
    }

    private fun <T : ViewBinding> BaseFragmentNfc<T>.enable(
        callback: NfcAdapter.ReaderCallback
    ) = enableFunction(callback)

    private fun BaseActivityNfc<*>.enable(callback: NfcAdapter.ReaderCallback) = enableFunction(callback)

    private fun BaseActivityNfc<*>.enableFunction(callback: NfcAdapter.ReaderCallback) {
        nfcAdapter?.let {
            val options = Bundle()

            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)

            it.enableReaderMode(
                this,
                callback,
                NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V,
                options
            )
        }
    }

    private fun Fragment.enableFunction(callback: NfcAdapter.ReaderCallback) {
        nfcAdapter?.let {
            val options = Bundle()

            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)

            it.enableReaderMode(
                requireActivity(),
                callback,
                NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V,
                options
            )
        }
    }

    private fun BottomSheetDialogFragment.setNfcAdapter() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(requireActivity())
    }

    fun BottomSheetDialogFragment.disableFunction() {
        nfcAdapter?.disableReaderMode(requireActivity())
        nfcAdapter = null
        bs = null
    }

    fun BottomSheetDialogFragment.enableFunction(callback: NfcAdapter.ReaderCallback) {
        setNfcAdapter()
        nfcAdapter?.let {
            val options = Bundle()

            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)

            it.enableReaderMode(
                requireActivity(),
                callback,
                NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V,
                options
            )
        }
    }

    fun BaseFragmentNfc<*>.disableNfc() = disableFunction()

    private fun Fragment.disableFunction() {
        nfcAdapter?.disableReaderMode(requireActivity())
        nfcAdapter = null
        bs = null
    }

    fun String.toNdefMessage(): NdefMessage? {
        val msg: String = this
        val languageCode: ByteArray
        val msgBytes: ByteArray
        try {
            languageCode = "en".toByteArray(charset("US-ASCII"))
            msgBytes = msg.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            return null
        }

        val messagePayload = ByteArray(
            1 + languageCode.size
                    + msgBytes.size
        )
        messagePayload[0] = 0x02.toByte() // status byte: UTF-8 encoding and

        System.arraycopy(
            languageCode, 0, messagePayload, 1,
            languageCode.size
        )
        System.arraycopy(
            msgBytes, 0, messagePayload, 1 + languageCode.size,
            msgBytes.size
        )

        val message: NdefMessage
        val records: Array<NdefRecord?> = arrayOfNulls(1)
        val textRecord = NdefRecord(
            NdefRecord.TNF_WELL_KNOWN,
            NdefRecord.RTD_TEXT, byteArrayOf(), messagePayload
        )
        records[0] = textRecord
        message = NdefMessage(records)
        return message
    }

    fun writeMessageToTag(
        context: Context,
        inputMessage: String?,
        nfcMessage: NdefMessage?,
        tag: Tag?
    ): Boolean {
        try {
            val ndef = Ndef.get(tag)

            ndef?.let {
                it.connect()
                if (it.maxSize < inputMessage.toString().toByteArray().size) {
                    runOnUiThread {
                        context.toast("Message is too large to write NFC tag")
                    }
                    return false
                } else if (it.isWritable) {
                    it.writeNdefMessage(nfcMessage)
                    it.close()
                    return true
                }
                return false
            }

            val formatableTag = NdefFormatable.get(tag)

            formatableTag?.let {
                try {
                    it.connect()
                    it.format(nfcMessage)
                    it.close()
                    return true
                } catch (e: IOException) {
                    return false
                }
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread {
                context.toast("Error write tag")
            }
        }
        return false
    }

/*    fun handleNdef(ndef: Ndef?, emptyAction: BaseFunction? = null, action: (String) -> Unit) {
        ndef?.let {
            it.connect()
            val message = it.ndefMessage
            val messageByteArray = message.toByteArray()

            val text = String(messageByteArray, StandardCharsets.UTF_8)

            action.invoke(text)
        } ?: emptyAction?.invoke()
    }*/

    fun createBarcodeBitmap(
        barcodeValue: String,
        @ColorInt barcodeColor: Int,
        @ColorInt backgroundColor: Int,
        widthPixels: Int,
        heightPixels: Int
    ): Bitmap {
        val bitMatrix = Code128Writer().encode(
            barcodeValue,
            BarcodeFormat.CODE_128,
            widthPixels,
            heightPixels
        )

        val pixels = IntArray(bitMatrix.width * bitMatrix.height)
        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] =
                    if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
            }
        }

        val bitmap = Bitmap.createBitmap(
            bitMatrix.width,
            bitMatrix.height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(
            pixels,
            0,
            bitMatrix.width,
            0,
            0,
            bitMatrix.width,
            bitMatrix.height
        )
        return bitmap
    }

    fun startA(tag: Tag?, onSuccess: (Pair<String, String>) -> Unit) {
        val nfcA = NfcA.get(tag)
        try {
            nfcA.connect()

            val transceive = nfcA.transceive(byteArrayOf(64, -64, 6, 0, 0, 0, 0))
            val hexString2 = getHexString(transceive, transceive.size)
            hexString2.logPrint("hexString2")

            SystemClock.sleep(400)

            if (!hexString2.contains("FAFF")) {
                "Error".logPrint("error")
            } else {
                val transceive1 = nfcA.transceive(byteArrayOf(64, -64, -124, 0, 0, 0, 0))
                val hexValue = getHexString(transceive1, transceive1.size)
                val v = strFormat(hexValue)
                "$v °C".logPrint("v_tag_result")
                onSuccess(v.toString() to hexValue)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun strFormat(str: String): Float {
        val resultTem: Float
        Log.d("str_result", str)
        val substring = str.substring(str.length - 4, str.length - 2)
        val substring1 = str.substring(str.length - 2, str.length)
        val newstr = substring1 + substring
        val stringToBinary = hexStringToBinary(newstr)

        stringToBinary.logPrint("tesi_string_binary")

        val hexString = binaryString2hexString(
            "0000000" + stringToBinary.substring(stringToBinary.length - 10).substring(1)
        )
        resultTem = if (substring1.toCharArray()[1] >= '2') {
            @SuppressLint("RestrictedApi") val d =
                (-(SupportMenu.USER_MASK - newstr.toInt(16) and 1023) - 1).toDouble()
            java.lang.Double.isNaN(d)
            (d / 4.0).toFloat()
        } else {
            val parseInt = hexString.toInt(16).toDouble()
            java.lang.Double.isNaN(parseInt)
            (parseInt / 4.0).toFloat()
        }

        return resultTem
    }

    private fun getHexString(raw: ByteArray, len: Int): String {
        val hex = ByteArray(len * 2)
        var index = 0
        var pos = 0
        for (b in raw) {
            if (pos >= len) break
            pos++
            val v: Int = b.toInt() and 255
            val index2 = index + 1
            val bArr = HEX_CHAR_TABLE
            hex[index] = bArr[v ushr 4]
            index = index2 + 1
            hex[index2] = bArr[v and 15]
        }
        return String(hex)
    }

    fun hexStringToBinary(hex: String): String {
        val hex2 = hex.uppercase(Locale.getDefault())
        val result = StringBuilder()
        val max = hex2.length
        for (i in 0 until max) {
            val c = hex2[i]
            when (c) {
                '0' -> result.append("0000")
                '1' -> result.append("0001")
                '2' -> result.append("0010")
                '3' -> result.append("0011")
                '4' -> result.append("0100")
                '5' -> result.append("0101")
                '6' -> result.append("0110")
                '7' -> result.append("0111")
                '8' -> result.append("1000")
                '9' -> result.append("1001")
                else -> when (c) {
                    'A' -> result.append("1010")
                    'B' -> result.append("1011")
                    'C' -> result.append("1100")
                    'D' -> result.append("1101")
                    'E' -> result.append("1110")
                    'F' -> result.append("1111")
                }
            }
        }
        return result.toString()
    }

    fun binaryString2hexString(bString: String?): String {
        if (bString != null && bString != "") {
            val length = bString.length % 8
        }
        val tmp = StringBuffer()
        var i = 0
        while (i < bString!!.length) {
            var iTmp = 0
            for (j in 0..3) {
                iTmp += bString.substring(i + j, i + j + 1).toInt() shl 4 - j - 1
            }
            tmp.append(Integer.toHexString(iTmp))
            i += 4
        }
        return tmp.toString()
    }

    fun handleNdef(ndef: Ndef?, emptyAction: BaseFunction? = null, action: (TagModel) -> Unit) {
        ndef?.let {
            it.connect()

            it.ndefMessage?.let { message ->
                val messageByteArray = message.toByteArray()

                runOnUiThread {
                    messageByteArray?.let {
                        val str = String(messageByteArray, StandardCharsets.UTF_8)

                        if (str.contains("code").not()) emptyAction?.invoke()
                        else {
                            val startIndex = str.indexOfFirst { char -> char == '{' }
                            val endIndex = str.indexOfLast { char -> char == '}' }

                            val subStringObject = str.substring(startIndex, endIndex + 1)

                            subStringObject.logPrint("objectJson")
                            val model = TagModel.fromJson(subStringObject)

                            action(model)
                        }
                    }
                }
            } ?: run {
                emptyAction?.invoke()
            }
        } ?: run {
            emptyAction?.invoke()
        }
    }

    /*fun predictNextTemp(context: Context, arr: FloatArray): Float {
        val tensorBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 3), DataType.FLOAT32)
        // Loading a float array:

        tensorBuffer.loadArray(arr)

        val model = Temperature.newInstance(context)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorBuffer.buffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        outputFeature0.getFloatValue(0).logPrint("output_buffer_result")

        // Releases model resources if no longer used.
        model.close()

        return outputFeature0.getFloatValue(0)
    }

    fun predictNextTemp5(context: Context, arr: FloatArray): Float {
        val tensorBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 5), DataType.FLOAT32)
        // Loading a float array:

        tensorBuffer.loadArray(arr)

        val model = Temperature5.newInstance(context)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 5), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorBuffer.buffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        outputFeature0.getFloatValue(0).logPrint("output_buffer_result")

        // Releases model resources if no longer used.
        model.close()

        return outputFeature0.getFloatValue(0)
    }*/

/*    fun predictWithRecursion(context: Context, arr: FloatArray, numRecursion: Int): Float {
        var response = 0f
        val list = ArrayList(arr.toMutableList())

        for (i in 0 until numRecursion) {
            val result = predictNextTemp(context, list.toFloatArray())

            list.removeAt(0)
            list.add(result)
            response = result
        }

        return response
    }

    fun predictWithRecursion5(context: Context, arr: FloatArray, numRecursion: Int): Float {
        var response = 0f
        val list = ArrayList(arr.toMutableList())

        for (i in 0 until numRecursion) {
            val result = predictNextTemp5(context, list.toFloatArray())

            list.removeAt(0)
            list.add(result)
            response = result
        }

        return response
    }*/
}
