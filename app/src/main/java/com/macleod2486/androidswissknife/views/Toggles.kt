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
package com.macleod2486.androidswissknife.views

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.macleod2486.androidswissknife.R
import com.macleod2486.androidswissknife.components.Flashlight
import com.macleod2486.androidswissknife.components.Location
import com.macleod2486.androidswissknife.components.Wifi

class Toggles : Fragment() {
    //Location
    lateinit var toggleLocation: Button
    lateinit var location: Location

    //Torch
    lateinit var toggleLight: Button
    lateinit var toggleLightListener: Flashlight

    //Wifi
    var toggleWifi: Button? = null
    var toggleWifiListener: Wifi? = null
    val CAMERA_CODE = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.toggles, container, false)
        toggleLight = view.findViewById<View>(R.id.toggleLight) as Button
        if (activity!!.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            toggleLightListener = Flashlight(requireActivity(), CAMERA_CODE)
            toggleLight.setOnClickListener(toggleLightListener)
        }
        toggleWifi = view.findViewById<View>(R.id.toggleWifi) as Button
        if (activity!!.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI)) {
            toggleWifiListener = Wifi(requireActivity())
            toggleWifi!!.setOnClickListener(toggleWifiListener)
        }
        toggleLocation = view.findViewById<View>(R.id.toggleGps) as Button
        if (activity!!.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            location = Location(requireActivity())
            toggleLocation.setOnClickListener(location)
        }
        return view
    }

    fun toggleLight() {
        toggleLightListener.toggleLight()
    }
}