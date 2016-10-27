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
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.macleod2486.androidswissknife.R;

public class NFCTool implements View.OnClickListener
{
    NfcAdapter adapter;

    FragmentActivity activity;

    public NFCTool(FragmentActivity activity)
    {
        this.activity = activity;
        adapter = NfcAdapter.getDefaultAdapter(activity.getApplicationContext());
    }

    @Override
    public void onClick(View view)
    {
        Log.i("NFCTool","Clicked");

        if(view.getId() == R.id.readNFC)
        {
            if (adapter == null)
            {
                Toast.makeText(this.activity, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
                return;
            }

            if (!adapter.isEnabled())
            {
                Toast.makeText(this.activity, "NFC is disabled", Toast.LENGTH_LONG).show();
                return;
            }

            read();
        }

        if(view.getId() == R.id.writeNFC)
        {
            Log.i("NFCTool","Writing");
            write();
        }
    }

    private void read()
    {
        Log.i("NFCTool","Read");
        Intent nfcIntent = new Intent(activity.getApplicationContext(),NFCActivity.class);
        nfcIntent.putExtra("NFCMode","read");
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, nfcIntent, 0);

        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);

        IntentFilter[] filterArray = new IntentFilter[] {filter};

        String [][] techListsArray = new String[][] { new String[] { MifareUltralight.class.getName(), Ndef.class.getName(), NfcA.class.getName()},
                new String[] { MifareClassic.class.getName(), Ndef.class.getName(), NfcA.class.getName()}};

        adapter.enableForegroundDispatch(activity, pendingIntent, filterArray, techListsArray);

        Toast.makeText(this.activity, "Please scan tag with device.", Toast.LENGTH_LONG).show();
    }

    private void write()
    {
        Log.i("NFCTool","Write");

        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                .setView(input)
                .setTitle("Write Mode")
                .setMessage("What do you want to be written to tag?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setUpWrite(input.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

        alertDialogBuilder.show();
    }

    private void setUpWrite(String message)
    {
        Log.i("NFCTool","Message received "+message);
        Intent nfcIntent = new Intent(activity.getApplicationContext(),NFCActivity.class);
        nfcIntent.putExtra("NFCMode","write");
        nfcIntent.putExtra("NFCMessage",message);
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, nfcIntent, 0);

        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);

        IntentFilter[] filterArray = new IntentFilter[] {filter};

        String [][] techListsArray = new String[][] { new String[] { MifareUltralight.class.getName(), Ndef.class.getName(), NfcA.class.getName()},
                new String[] { MifareClassic.class.getName(), Ndef.class.getName(), NfcA.class.getName()}};

        adapter.enableForegroundDispatch(activity, pendingIntent, filterArray, techListsArray);

        Toast.makeText(this.activity, "Please scan tag with device.", Toast.LENGTH_LONG).show();
    }
}