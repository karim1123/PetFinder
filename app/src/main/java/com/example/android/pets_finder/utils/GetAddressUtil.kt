package com.example.android.pets_finder.utils

import android.location.Address
import android.location.Geocoder
import android.util.Log
import java.io.IOException

object GetAddressUtil {
    private const val EMPTY_STRING = ""
    private const val SEPARATOR = ", "
    private const val LINE_BREAK = "\n"
    private const val MAX_RESUlt = 1
    private const val EXCEPTION = "get address exception"

    fun Geocoder.getContactAddress(latitude: Double, longitude: Double): String {
        var addressString = EMPTY_STRING
        try {
            val addressList: List<Address> =
                this.getFromLocation(latitude, longitude, MAX_RESUlt)
            if (addressList.isNotEmpty()) {
                val address = addressList[0]
                val sb = StringBuilder()
                for (i in 0 until address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append(LINE_BREAK)
                }
                val setAddress = { contactAddress: String?, stringBuilder: StringBuilder ->
                    if (contactAddress != null) {
                        if (contactAddress != address.postalCode) stringBuilder.append(
                            contactAddress
                        ).append(SEPARATOR) else stringBuilder.append(contactAddress)
                    }
                }
                setAddress(address.subAdminArea, sb)
                setAddress(address.thoroughfare, sb)
                setAddress(address.subThoroughfare, sb)
                // удаление разделителя в конце строки т.к. он не нужен
                addressString = sb.dropLast(SEPARATOR.length).toString()
            }
        } catch (e: IOException) {
            Log.d(EXCEPTION, e.toString())
        }
        return addressString
    }
}
