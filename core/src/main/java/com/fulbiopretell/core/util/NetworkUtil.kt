package com.fulbiopretell.core.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

fun isNetworkAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (isLollipop()) {
        cm.allNetworks.any { isConnected(cm.getNetworkInfo(it)!!) } == true
    } else {
        cm.allNetworkInfo.any { isConnected(it) } == true
    }

}

fun isConnected(network: NetworkInfo): Boolean {
    return (network.type == ConnectivityManager.TYPE_WIFI || network.type == ConnectivityManager.TYPE_MOBILE) &&
            network.isConnected &&
            network.isAvailable
}