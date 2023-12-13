package com.angfort.biotrakcoldchain.ui.base

import android.app.Dialog
import android.content.Context
import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.angfort.biotrakcoldchain.databinding.LoaderLayoutBinding

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    var loader: Dialog? = null

    private fun showLoader() {
        loader = DialogManager.setLoader(this)
        loader?.show()
    }

    private fun dismissLoader() {
        loader?.dismiss()
        loader = null
    }

    fun loader(isLoading: Boolean? = false) {
        if (isLoading == true) showLoader()
        else dismissLoader()
    }

    abstract fun setInflater(): T
    open fun T.setUI() {}
    open fun T.onAttachObservers() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = setInflater()
        setContentView(binding.root)

        with(binding) {
            lifecycleScope.launchWhenCreated { onAttachObservers() }
            setUI()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

object DialogManager {
    /**
     * Set simple loader
     * @param context: Context
     * @return Dialog instance
     */
    fun setLoader(context: Context): Dialog {

        val dialog = Dialog(context)

        val view = LoaderLayoutBinding.inflate(LayoutInflater.from(context), null, false)
        dialog.setContentView(view.root)

        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }
}

abstract class BaseActivityNfc<T : ViewBinding> : BaseActivity<T>(), NfcAdapter.ReaderCallback