package com.angfort.biotrakcoldchain.manager

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecurityManager {
    var sharedPreferences: SharedPreferences? = null

    fun initSharedPref(context: Context, prefName: String) {
        if (sharedPreferences == null) {
            val spec = KeyGenParameterSpec.Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).setKeySize(256).build()

            val masterKey = MasterKey.Builder(context).setKeyGenParameterSpec(spec).build()

            sharedPreferences = EncryptedSharedPreferences.create(
                context,
                prefName,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    fun putIntoShared(vararg pair: Pair<Any, Any?>) {
        val edit = sharedPreferences?.edit()
        fun putString(key: String, value: String) = edit?.putString(key, value)?.apply()
        fun putInt(key: String, value: Int) = edit?.putInt(key, value)?.apply()
        fun putBoolean(key: String, value: Boolean) = edit?.putBoolean(key, value)?.apply()
        fun putFloat(key: String, value: Float) = edit?.putFloat(key, value)?.apply()
        fun putLong(key: String, value: Long) = edit?.putLong(key, value)?.apply()

        pair.forEach {
            val keyValue = it.first
            val key = if (keyValue is PrefEnum)
                keyValue.value
            else
                    keyValue.toString()

            when (val value = it.second) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> putString(key, value.toJson())
            }
        }
    }

    fun containsKey(keyValue: Any) = sharedPreferences?.contains(if (keyValue is PrefEnum) keyValue.value else keyValue.toString())

    fun clearAll() {
        sharedPreferences?.edit()?.clear()?.apply()
    }

    inline fun <reified T> getFromShared(keyValue: Any): T? {
        val key = if (keyValue is PrefEnum) keyValue.value else keyValue.toString()
        val value = sharedPreferences?.all?.get(key)
        return if (value is T) value
        else null
    }
}

enum class PrefEnum(val value: String) {
    THRESHOLD_MIN("threshold_min"),
    THRESHOLD_MAX("threshold_max")
}