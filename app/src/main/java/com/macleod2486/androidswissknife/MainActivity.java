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
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.macleod2486.androidswissknife.views.NFC;
import com.macleod2486.androidswissknife.views.Toggles;

public class MainActivity extends AppCompatActivity
{
    int index = 0;

    //Request codes
    final int CAMERA_CODE = 0;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;

    //Different fragments
    Toggles toggleFrag = new Toggles();
    NFC nfcFrag = new NFC();

    //Manages what the back button does
    @Override
    public void onBackPressed()
    {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            Log.i("Main","Drawer closed");
            drawer.closeDrawers();
        }

        if(index == 0 && !toggleFrag.isAdded())
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, toggleFrag, "main").commit();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Configures the drawer
        drawer = (DrawerLayout)findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.mipmap.ic_drawer, R.string.drawer_open, R.string.drawer_close)
        {
            public void onDrawerClosed(View view)
            {
                getSupportActionBar().setTitle(R.string.drawer_close);
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView)
            {
                getSupportActionBar().setTitle(R.string.drawer_open);
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(drawerToggle);
        drawer.setDrawerLockMode(drawer.LOCK_MODE_UNLOCKED);

        //Sets up the listview within the drawer
        String [] menuList = getResources().getStringArray(R.array.menu);
        ListView list = (ListView)findViewById(R.id.optionList);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                Log.i("MainActivity","Position "+position);
                if(position == 0)
                {
                    index = 0;
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, toggleFrag, "toggles").commit();
                }
                else if(position == 1)
                {
                    index = 1;
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, nfcFrag, "nfc").commit();
                }

                drawer.closeDrawers();
            }
        });

        //Make the actionbar clickable to bring out the drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Displays the first fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, toggleFrag, "toggles").commit();
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
                    toggleFrag.toggleLight();
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