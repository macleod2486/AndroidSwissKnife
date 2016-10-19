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
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.macleod2486.androidswissknife.R;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

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

        tagResult = (TextView)findViewById(R.id.tagResult);

        onNewIntent(getIntent());
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        mode = extras.getString("NFCMode");

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if(mode == null)
        {
            mode = "read";
        }

        Log.i("NFCActivity","Mode "+mode);
        Log.i("NFCActivity","Intent received");

        if(mode.equals("read"))
        {
            String [] tlist = tag.getTechList();
            Log.i("NFCActivity", "Content "+tag.describeContents());

            tagResult.append("Tech used: ");
            for(int index = 0; index < tlist.length; index++)
            {
                tagResult.append(tlist[index]+", ");
            }
            tagResult.append("\nContents: " + tag.describeContents());
        }

        if(mode.equals("write"))
        {

            try
            {
                // Get UTF-8 byte
                String content = "This is a test";

                byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");

                byte[] text = content.getBytes("UTF-8"); // Content in UTF-8

                int langSize = lang.length;

                int textLength = text.length;

                ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);

                payload.write((byte) (langSize & 0x1F));

                payload.write(lang, 0, langSize);

                payload.write(text, 0, textLength);

                NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

                NdefMessage message = new NdefMessage(new NdefRecord[]{record});

                Ndef ndefTag = Ndef.get(tag);
                if (ndefTag == null)
                {
                    NdefFormatable nForm = NdefFormatable.get(tag);
                    if (nForm != null)
                    {
                        nForm.connect();
                        nForm.format(message);
                        nForm.close();
                    }
                }
                else
                {
                    ndefTag.connect();
                    ndefTag.writeNdefMessage(message);
                    ndefTag.close();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}