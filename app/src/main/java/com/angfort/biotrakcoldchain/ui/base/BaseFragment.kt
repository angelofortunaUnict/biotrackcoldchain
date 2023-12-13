package com.angfort.biotrakcoldchain.ui.base

import android.app.Dialog
import android.content.Context
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBinding
import com.angfort.biotrakcoldchain.manager.BaseFunction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    private val job = Job()

    private var _binding: T? = null
    protected val binding get() = _binding!!

    abstract fun setInflater(): T
    open fun T.setUI() {}

    open fun T.onBackPressed(): BaseFunction? = null
    open fun T.onAttachObservers() {}
    open fun getResultNavigation() {}

    var loader: Dialog? = null

    fun showLoader() {
        loader = DialogManager.setLoader(requireContext())
        loader?.show()
    }

    fun dismissLoader() {
        loader?.let {
            it.dismiss()
            loader = null
        }
    }

    fun loader(isLoading: Boolean? = false) {
        if (isLoading == true) showLoader()
        else dismissLoader()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = setInflater()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated { onAttachObservers() }
            setUI()

            onBackPressed()?.let { onBackPressed(it) }
            getResultNavigation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun onBackPressed(func: () -> Unit) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                func.invoke()
            }
        })
    }

    fun hideKeyboard(context: Context, windowToken: IBinder) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
    }
}

abstract class BaseFragmentNfc<T : ViewBinding> : BaseFragment<T>(), NfcAdapter.ReaderCallback