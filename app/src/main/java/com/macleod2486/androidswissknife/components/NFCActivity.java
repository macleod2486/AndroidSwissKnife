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

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.macleod2486.androidswissknife.R;

public class NFCActivity extends Activity
{
    TextView tagResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i("NFCActivity","On create");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nfctaglayout);

        tagResult = (TextView)findViewById(R.id.tagResult);

        onNewIntent(getIntent());
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        Log.i("NFCActivity","Intent received");
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String [] tlist = tag.getTechList();
        Log.i("NFCActivity", "Content "+tag.describeContents());
        //tagResult.setText("Tech used: "+tlist.length+"\nContents: " + tag.describeContents());

        tagResult.append("Tech used: ");
        for(int index = 0; index < tlist.length; index++)
        {
            tagResult.append(tlist[index]+", ");
        }
        tagResult.append("\nContents: " + tag.describeContents());
    }
}