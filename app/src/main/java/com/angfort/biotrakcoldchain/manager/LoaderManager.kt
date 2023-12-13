package com.angfort.biotrakcoldchain.manager

import android.app.Dialog
import android.content.Context
import com.angfort.biotrakcoldchain.ui.base.DialogManager

interface ILoaderManager {
    var loader: Dialog?
    fun showLoader(context: Context)
    fun dismissLoader(context: Context)
}

class LoaderManager: ILoaderManager {
    override var loader: Dialog? = null
    override fun showLoader(context: Context) {
        loader = DialogManager.setLoader(context)
        loader?.show()
    }

    override fun dismissLoader(context: Context) {
        loader?.let {
            it.dismiss()
            loader = null
        }
    }
}