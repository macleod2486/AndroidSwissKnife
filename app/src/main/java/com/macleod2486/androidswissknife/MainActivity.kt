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
package com.macleod2486.androidswissknife

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActionBarDrawerToggle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.macleod2486.androidswissknife.views.NFC
import com.macleod2486.androidswissknife.views.Toggles

class MainActivity : AppCompatActivity() {
    var index = 0

    //Request codes
    val CAMERA_CODE = 0
    private var drawer: DrawerLayout? = null
    private var drawerToggle: ActionBarDrawerToggle? = null

    //Different fragments
    var toggleFrag = Toggles()
    var nfcFrag = NFC()

    //Manages what the back button does
    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            Log.i("Main", "Drawer closed")
            drawer!!.closeDrawers()
        }
        if (index == 0 && !toggleFrag.isAdded) {
            supportFragmentManager.beginTransaction().replace(R.id.container, toggleFrag, "main").commit()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Configures the drawer
        drawer = findViewById<View>(R.id.drawer) as DrawerLayout
        drawerToggle = object : ActionBarDrawerToggle(this, drawer, R.mipmap.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerClosed(view: View) {
                supportActionBar!!.setTitle(R.string.drawer_close)
                super.onDrawerClosed(view)
            }

            override fun onDrawerOpened(drawerView: View) {
                supportActionBar!!.setTitle(R.string.drawer_open)
                super.onDrawerOpened(drawerView)
            }
        }
        drawer!!.setDrawerListener(drawerToggle)
        drawer!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        //Sets up the listview within the drawer
        val menuList = resources.getStringArray(R.array.menu)
        val list = findViewById<View>(R.id.optionList) as ListView
        list.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, menuList)
        list.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            Log.i("MainActivity", "Position $position")
            if (position == 0) {
                index = 0
                supportFragmentManager.beginTransaction().replace(R.id.container, toggleFrag, "toggles").commit()
            } else if (position == 1) {
                index = 1
                supportFragmentManager.beginTransaction().replace(R.id.container, nfcFrag, "nfc").commit()
            }
            drawer!!.closeDrawers()
        }

        //Make the actionbar clickable to bring out the drawer
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        //Displays the first fragment
        supportFragmentManager.beginTransaction().replace(R.id.container, toggleFrag, "toggles").commit()
    }

    //Toggles open the drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawer!!.isDrawerOpen(Gravity.START)) {
            drawer!!.closeDrawers()
        } else {
            drawer!!.openDrawer(Gravity.START)
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toggleFrag.toggleLight()
                } else {
                    Toast.makeText(this, "Need to enable all wifi permissions", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}