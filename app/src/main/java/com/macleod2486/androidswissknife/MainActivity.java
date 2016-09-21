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

package com.macleod2486.androidswissknife;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.macleod2486.androidswissknife.components.Flashlight;
import com.macleod2486.androidswissknife.components.Wifi;

public class MainActivity extends AppCompatActivity
{
    //Torch
    Button toggleLight;
    Flashlight toggleLightListener;

    //Wifi
    Button toggleWifi;
    Wifi toggleWifiListener;

    //Request codes
    final int CAMERA_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toggleLight = (Button)this.findViewById(R.id.toggleLight);
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
        {
            toggleLightListener = new Flashlight(this, CAMERA_CODE);
            toggleLight.setOnClickListener(toggleLightListener);
        }

        toggleWifi = (Button)this.findViewById(R.id.toggleWifi);
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI))
        {
            toggleWifiListener = new Wifi(this);
            toggleWifi.setOnClickListener(toggleWifiListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case CAMERA_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    toggleLightListener.toggleLight();
                }
                else
                {
                    Toast.makeText(this,"Need to enable all wifi permissions", Toast.LENGTH_SHORT).show();
                }

                return;
            }
        }

    }

}