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

import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;

import com.macleod2486.androidswissknife.R;

public class NFCCallback implements NfcAdapter.ReaderCallback
{
    FragmentActivity activity;

    public NFCCallback(FragmentActivity activity)
    {
        this.activity = activity;
        Log.i("NFCCallback", "Initalized ");
    }

    String nfcData = "";

    @Override
    public void onTagDiscovered(Tag tag)
    {

        EditText tagResult = activity.findViewById(R.id.textEntry);

        try
        {
            Ndef ndef = Ndef.get(tag);

            if (ndef != null)
            {
                NdefMessage ndefMesg = ndef.getCachedNdefMessage();
                if (ndefMesg != null)
                {
                    nfcData = "Message: " + ndefMesg.toString();
                    Log.i("NFCCallback", nfcData);
                }
            }
            else
            {
                nfcData = "Attempting to format";
                Log.i("NFCCallback","Attempting to format");

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        tagResult.setText("");
                        tagResult.append("Attempting to format");
                    }
                });

                NdefFormatable format = NdefFormatable.get(tag);

                if(format != null)
                {
                    format.connect();

                    if(format.isConnected())
                    {
                        try
                        {
                            format.format(null);
                            nfcData = "Formatted";
                            Log.i("NFCCallback", "Formatted");
                        }
                        catch(Exception e)
                        {
                            nfcData = "Error occurred in formatting";
                            e.printStackTrace();
                        }

                        format.close();
                    }
                }
                else
                {
                    Log.i("NFCCallback", "Tag not found");
                    nfcData = "Tag not found or formatted";
                }
            }

            if (tagResult != null)
            {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        tagResult.setText("");
                        Log.i("NFCCallback", "Cleared");
                        tagResult.append(nfcData);
                    }
                });
            }
        }
        catch (Exception e)
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    tagResult.setText("");
                    tagResult.append("Error reading tag");
                }
            });

            e.printStackTrace();
        }
    }
}
