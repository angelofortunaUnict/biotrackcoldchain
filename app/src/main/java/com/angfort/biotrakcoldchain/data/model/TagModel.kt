package com.angfort.biotrakcoldchain.data.model

import android.os.Parcelable
import com.angfort.biotrakcoldchain.manager.serializeToMap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TagModel(
    val code: String? = null,
    val cf: String? = null, // Patient's cf
    val cr: String? = null, // Doctor code
    val ty: String? = null, // Typology
    val ts1: String? = null, // When it was taken
    val ts2: String? = null,
    val ts3: String? = null
) : Parcelable {
    companion object {
        fun fromJson(json: String): TagModel {
            return Gson().fromJson(json, object : TypeToken<TagModel>() {}.type)
        }
    }

    fun hasData() = this.serializeToMap().values.size > 1
    fun toMap() = this.serializeToMap()
}
