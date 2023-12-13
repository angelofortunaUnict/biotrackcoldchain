package com.angfort.biotrakcoldchain.manager

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import kotlin.math.roundToInt

/**
 * @author TW - AF
 */

typealias BaseFunction = () -> Unit

fun View.getString(@StringRes id: Int) = this.resources.getString(id)

/**
 * Navigation controller: go back on the stack
 * From any fragment
 */
fun Fragment.goBack() {
    findNavController().popBackStack()
}

fun Double.roundToFirstDecimal(): Double {
    return ((this * 10.0).roundToInt() / 10.0)
}

fun Float.roundToFirstDecimal(): Float {
    return ((this * 10.0).roundToInt() / 10.0f)
}

fun TextInputLayout.text(text: String) {
    editText?.setText(text)
}

fun TextInputLayout.doAfterTextChanged(action: (String?) -> Unit) {
    editText?.doAfterTextChanged {
        action(it.toString())
    }
}

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, text, duration).show()

/**
 * Hide keyboard and request focus;
 * Call this function from all View instance.
 */
fun View.hideKeyboardOnClick() {
    click {
        val inputMethodManager =
            this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
        this.requestFocus()
    }
}

/**
 * Set on click listener
 * ```
 * button.click {
 *  [do something]
 * }
 * ```
 */
fun View.click(func: BaseFunction?) {
    setOnClickListener {
        func?.invoke()
    }
}

/**
 * Define RecyclerView's vertical orientation
 * @param adapter RecyclerView adapter
 */
fun RecyclerView.vertical(
    adapter: RecyclerView.Adapter<*>? = null,
    reverseLayout: Boolean = false
) {
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, reverseLayout)
    (layoutManager as LinearLayoutManager).isAutoMeasureEnabled = false
    this.adapter = adapter
}

/**
 * Set view's visibility visible
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Set view's visibility gone
 */
fun View.gone() {
    visibility = View.GONE
}

fun TextView.html(@StringRes value: Int) {
    text = Utils.setHtmlText(getString(value))
}

fun <T> T.serializeToMap(): Map<String, Any> {
    return convert()
}

//convert an object of type I to type O
inline fun <I, reified O> I.convert(): O {
    val json = Gson().toJson(this)
    return Gson().fromJson(json, object : TypeToken<O>() {}.type)
}

/**
 * Utils class
 * @author TW - AF
 */
object Utils {

    fun setHtmlText(htmlText: String?): Spanned? {
        return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    }

    /**
     * Execute a function with delay
     * @param delay: Long, delay time
     * @param func: function should be executed
     */
    fun delay(delay: Long, func: BaseFunction) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            func.invoke()
        }, delay)
    }

}

/**
 * Convert an object into its JSON representation
 */
fun Any?.toJson(): String = Gson().toJson(this)

/**
 * Print a String or an object using Logger
 * @param tag: String
 */
fun Any?.logPrint(tag: String = "logger_t") {
    if (this is String) Logger.t(tag).i(this)
    else Logger.t(tag).i(toJson())
}

enum class DateFormatEnum(val pattern: String) {
    DATE("dd/MM/yyyy"),
    DATE_TIME("dd/MM/yyyy hh:mm"),
    TIME("hh:mm")
}

object ThreadUtils {
    private val handler = Handler(Looper.getMainLooper())

    fun runOnUiThread(action: () -> Unit) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post(action)
        } else {
            action.invoke()
        }
    }
}