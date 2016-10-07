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

package com.macleod2486.androidswissknife.views;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.macleod2486.androidswissknife.R;
import com.macleod2486.androidswissknife.components.Flashlight;
import com.macleod2486.androidswissknife.components.Location;
import com.macleod2486.androidswissknife.components.Wifi;

public class Toggles extends Fragment
{
    //Location
    Button toggleLocation;
    Location location;

    //Torch
    Button toggleLight;
    Flashlight toggleLightListener;

    //Wifi
    Button toggleWifi;
    Wifi toggleWifiListener;

    final int CAMERA_CODE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.toggles, container, false);

        toggleLight = (Button)view.findViewById(R.id.toggleLight);
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
        {
            toggleLightListener = new Flashlight(getActivity(), CAMERA_CODE);
            toggleLight.setOnClickListener(toggleLightListener);
        }

        toggleWifi = (Button)view.findViewById(R.id.toggleWifi);
        if(getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI))
        {
            toggleWifiListener = new Wifi(getActivity());
            toggleWifi.setOnClickListener(toggleWifiListener);
        }

        toggleLocation = (Button)view.findViewById(R.id.toggleGps);
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS))
        {
            location = new Location(getActivity());
            toggleLocation.setOnClickListener(location);
        }

        return view;
    }

    public void toggleLight()
    {
        toggleLightListener.toggleLight();
    }
}
