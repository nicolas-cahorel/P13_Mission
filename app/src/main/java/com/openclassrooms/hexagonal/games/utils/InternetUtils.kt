package com.openclassrooms.hexagonal.games.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * A utility class to check the availability of an internet connection.
 * This class is used to determine if the device is connected to a network
 * via Wi-Fi or cellular data.
 */
class InternetUtils @Inject constructor(@ApplicationContext private val context: Context) {

    /**
     * Checks whether the device has an active internet connection.
     *
     * This method uses the system's ConnectivityManager to get information about
     * the device's current network and its capabilities. It returns true if the
     * device is connected to a Wi-Fi or cellular network, indicating that internet
     * access is available. Otherwise, it returns false.
     *
     * @return Boolean value indicating whether the internet is available.
     */
    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

}