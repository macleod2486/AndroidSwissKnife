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
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import com.macleod2486.androidswissknife.R;

public class NFCActivity extends Activity
{
    TextView tagResult;

    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i("NFCActivity","On create");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nfctaglayout);

        onNewIntent(getIntent());

        mode = getIntent().getStringExtra("NFCMode");

        Log.i("NFCActivity", "NFCMode "+mode);

        Log.i("NFCActivity","Tag "+getIntent().getAction());
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Log.i("NFCActivity", "Intent received");

        if (intent != null && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
        {
            Parcelable[] rawMessages =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null)
            {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++)
                {
                    messages[i] = (NdefMessage) rawMessages[i];
                    Log.i("NFCActivity", "Received message "+messages[0]);
                }
            }
        }
    }

}