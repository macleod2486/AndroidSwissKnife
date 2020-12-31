/*
    AndroidSwissKnife
    Copyright (C) 2016  macleod2486

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see [http://www.gnu.org/licenses/].
 */
package com.macleod2486.androidswissknife.components

import android.content.Context
import android.net.wifi.WifiManager
import androidx.fragment.app.FragmentActivity
import android.util.Log
import android.view.View

class Wifi(var activity: FragmentActivity) : View.OnClickListener {
    var wifiManager: WifiManager
    override fun onClick(view: View) {
        Log.i("Wifi", "Current toggle " + wifiManager.isWifiEnabled)
        if (wifiManager.isWifiEnabled) {
            turnOffWifi()
        } else {
            turnOnWifi()
        }
        Log.i("Wifi", "Click captured")
    }

    fun toggleLight() {
        if (wifiManager.isWifiEnabled) {
            turnOffWifi()
        } else {
            turnOnWifi()
        }
    }

    private fun turnOnWifi() {
        Log.i("Wifi", "Toggling on wifi")
        wifiManager.isWifiEnabled = true
    }

    private fun turnOffWifi() {
        Log.i("Wifi", "Toggling off wifi")
        wifiManager.isWifiEnabled = false
    }

    init {
        wifiManager = activity.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }
}