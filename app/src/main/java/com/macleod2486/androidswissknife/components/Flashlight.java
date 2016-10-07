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

package com.macleod2486.androidswissknife.components;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Flashlight implements View.OnClickListener
{
    FragmentActivity activity;
    boolean torchOn = false;
    int requestCode;

    Camera cam;
    Camera.Parameters p;

    public Flashlight(FragmentActivity activity, int code)
    {
        this.requestCode = code;
        this.activity = activity;
    }

    @Override
    public void onClick(View view)
    {
        Log.i("Flashlight","Current toggle "+torchOn);

        if(checkPermissions())
        {
            if(torchOn)
            {
                turnOffLight();
            }
            else
            {
                turnOnLight();
            }
        }
    }

    public void toggleLight()
    {
        if(torchOn)
        {
            turnOffLight();
        }
        else
        {
            turnOnLight();
        }
    }

    private void turnOnLight()
    {
        Log.i("Flashlight","Toggling on light");

        try
        {
            cam = Camera.open();
            p = cam.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            SurfaceTexture mPreviewTexture = new SurfaceTexture(0);
            cam.setPreviewTexture(mPreviewTexture);
            cam.startPreview();
            torchOn = true;
        }
        catch (Exception ex)
        {
            torchOn = false;
            Log.e("Flashlight",ex.toString());
        }
    }

    private void turnOffLight()
    {
        Log.i("Flashlight","Toggling off light");

        torchOn = false;

        cam.stopPreview();
        cam.release();
    }

    private boolean checkPermissions()
    {
        boolean allowed = false;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA))
            {
                Toast.makeText(activity.getApplicationContext(),"Need to enable camera permissions", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},requestCode);
            }
        }
        else
        {
            allowed = true;
        }

        return allowed;
    }
}
