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

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Toast

class Flashlight(var activity: FragmentActivity, var requestCode: Int) : View.OnClickListener {
    var torchOn = false
    var cam: Camera? = null
    var p: Camera.Parameters? = null
    override fun onClick(view: View) {
        Log.i("Flashlight", "Current toggle $torchOn")
        if (checkPermissions()) {
            if (torchOn) {
                turnOffLight()
            } else {
                turnOnLight()
            }
        }
    }

    fun toggleLight() {
        if (torchOn) {
            turnOffLight()
        } else {
            turnOnLight()
        }
    }

    private fun turnOnLight() {
        Log.i("Flashlight", "Toggling on light")
        try {
            cam = Camera.open()
            p = cam.getParameters()
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
            cam.setParameters(p)
            val mPreviewTexture = SurfaceTexture(0)
            cam.setPreviewTexture(mPreviewTexture)
            cam.startPreview()
            torchOn = true
        } catch (ex: Exception) {
            torchOn = false
            Log.e("Flashlight", ex.toString())
        }
    }

    private fun turnOffLight() {
        Log.i("Flashlight", "Toggling off light")
        torchOn = false
        cam!!.stopPreview()
        cam!!.release()
    }

    private fun checkPermissions(): Boolean {
        var allowed = false
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                Toast.makeText(activity.applicationContext, "Need to enable camera permissions", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), requestCode)
            }
        } else {
            allowed = true
        }
        return allowed
    }
}