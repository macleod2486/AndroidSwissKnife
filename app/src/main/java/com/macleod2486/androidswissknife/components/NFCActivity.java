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
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import com.macleod2486.androidswissknife.R;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
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

        onNewIntent(getIntent());

        Log.i("NFCActivity","Tag "+getIntent().getAction());
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        mode = extras.getString("NFCMode");
        String bundleMessage = extras.getString("NFCMessage");

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if(mode == null)
        {
            mode = "read";
        }

        Log.i("NFCActivity","Mode "+mode);
        Log.i("NFCActivity","Intent received");

        tagResult = (TextView)findViewById(R.id.tagResult);
        tagResult.setText("");

        if(mode.equals("read"))
        {
            boolean validTech = false;

            for(int index = 0; index < tag.getTechList().length; index++)
            {
                Log.i("NFCActivity","Tech list "+tag.getTechList()[index]);

                if(!validTech) {
                    techLoop:
                    switch (tag.getTechList()[index]) {
                        case "android.nfc.tech.Ndef":
                        case "android.nfc.tech.NdefFormatable": {
                            readNDEFtag(tag, intent);
                            validTech = true;
                            break techLoop;
                        }
                        case "android.nfc.tech.MifareUltralight": {
                            readMifareUltralight(tag);
                            validTech = true;
                            break techLoop;
                        }
                        default: {
                            Log.i("NFCActivity", "Defaulting");
                            break;
                        }
                    }
                }
                else
                    break;
            }

            if(!validTech)
            {
                tagResult.setText("We don't support this NFC tag");
            }
        }

        if(mode.equals("write"))
        {
            boolean validTech = false;

            for(int index = 0; index < tag.getTechList().length; index++)
            {
                Log.i("NFCActivity","Tech list "+tag.getTechList()[index]);

                if(!validTech)
                {
                    techLoop:
                    switch (tag.getTechList()[index])
                    {
                        case "android.nfc.tech.Ndef":
                        case "android.nfc.tech.NdefFormatable":
                        {
                            writeNDEFtag(tag, bundleMessage);
                            validTech = true;
                            break techLoop;
                        }
                        case "android.nfc.tech.MifareUltralight":
                        {
                            writeMifareUltralight(tag, bundleMessage);
                            validTech = true;
                            break techLoop;
                        }
                        default:
                        {
                            Log.i("NFCActivity", "Defaulting");
                            break techLoop;
                        }
                    }
                }
                else
                    break;
            }

            if(!validTech)
            {
                tagResult.setText("We don't support this NFC tag");
            }
        }
    }

    private void readNDEFtag(Tag tag, Intent intent)
    {
        try
        {
            Ndef ndefTag = Ndef.get(tag);

            ndefTag.connect();

            if(ndefTag.isConnected())
            {
                Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                NdefRecord relayRecord = ((NdefMessage)rawMsgs[0]).getRecords()[0];
                String nfcData = new String(relayRecord.getPayload());

                tagResult.append(nfcData);
            }
            else
            {
                tagResult.append("Error reading tag");
            }

            ndefTag.close();
        }
        catch (Exception e)
        {
            tagResult.append("Error reading tag");
            e.printStackTrace();
        }
    }

    private void writeNDEFtag(Tag tag, String bundleMessage)
    {
        try
        {
            // Get UTF-8 byte
            Log.i("NFCActivity","Message "+bundleMessage);
            String content = bundleMessage;

            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");

            byte[] text = content.getBytes("UTF-8"); // Content in UTF-8

            int langSize = lang.length;

            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);

            payload.write(text, 0, textLength);

            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

            NdefMessage message = new NdefMessage(new NdefRecord[]{record});

            Ndef ndefTag = Ndef.get(tag);
            if (ndefTag == null)
            {
                Log.i("NFCActivity","Formatting tag");
                NdefFormatable nForm = NdefFormatable.get(tag);
                if (nForm != null)
                {
                    try
                    {
                        nForm.connect();

                        try
                        {
                            if(nForm.isConnected())
                            {
                                nForm.format(message);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    finally
                    {
                        nForm.close();
                    }
                }
            }
            else
            {
                Log.i("NFCActivity","Writing tag");
                ndefTag.connect();
                if(ndefTag.isConnected())
                {
                    ndefTag.writeNdefMessage(message);
                }
                ndefTag.close();
            }

            tagResult.append("\nTag written");
        }
        catch(Exception e)
        {
            tagResult.append("\nTag failed to write");
            e.printStackTrace();
        }
    }

    private void readMifareUltralight(Tag tag)
    {
        try
        {
            MifareUltralight mifareUltralightTag = MifareUltralight.get(tag);
            mifareUltralightTag.connect();

            if(mifareUltralightTag.isConnected())
            {
                byte[] result;
                String stringResult;
                for(int index = 4; index < 16; index++)
                {
                    result = mifareUltralightTag.readPages(index);
                    stringResult = new String(result, Charset.forName("US-ASCII"));
                    tagResult.append(stringResult);
                }
            }
            else
            {
                tagResult.append("Error reading tag");
            }

            mifareUltralightTag.close();
        }
        catch (Exception e)
        {
            tagResult.append("Error reading tag");
            e.printStackTrace();
        }
    }

    private void writeMifareUltralight(Tag tag, String bundleMessage)
    {
        try
        {
            MifareUltralight mifareUltralightTag = MifareUltralight.get(tag);
            mifareUltralightTag.connect();

            if (mifareUltralightTag.isConnected())
            {
                double numberOfPages = bundleMessage.length() / 4;

                if(numberOfPages % 4 != 0) numberOfPages +=1;
                if(numberOfPages > 12) numberOfPages = 12;

                int indexShift = 0;
                String messageToWrite;

                for(int index = 0; index < numberOfPages; index++)
                {
                    messageToWrite = "";

                    for (int secIndex = 0; secIndex < 4 && secIndex + indexShift < bundleMessage.length(); secIndex++)
                    {
                        messageToWrite = messageToWrite + bundleMessage.charAt(secIndex + indexShift);
                    }

                    Log.i("NFCActivity","Message "+messageToWrite + " Length "+messageToWrite.length() + " indexShift "+indexShift + " index "+index);

                    mifareUltralightTag.writePage(index + 4, messageToWrite.getBytes(Charset.forName("US-ASCII")));
                    indexShift += 4;
                }
            }

            mifareUltralightTag.close();
        }
        catch(Exception e)
        {
            tagResult.append("Error writing tag");
            e.printStackTrace();
        }
    }
}