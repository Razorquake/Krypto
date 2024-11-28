package com.razorquake.krypto.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.razorquake.krypto.data.remote.dto.Roi

@ProvidedTypeConverter
class CoinTypeConvertor {
    @TypeConverter
    fun fromRoi(roi: Roi?): String? {
        return roi?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toRoi(roiString: String?): Roi? {
        return roiString?.let {
            val type = object : TypeToken<Roi>() {}.type
            Gson().fromJson(it, type)
        }
    }
}