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

package com.macleod2486.androidswissknife.views;

import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.macleod2486.androidswissknife.R;
import com.macleod2486.androidswissknife.components.NFCCallback;
import com.macleod2486.androidswissknife.components.NFCTool;

public class NFC extends Fragment
{
    Button writeNFC;
    Button clearText;

    NFCTool tool;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.nfc, container, false);

        tool = new NFCTool(getActivity());

        writeNFC = (Button)view.findViewById(R.id.writeNFC);
        writeNFC.setOnClickListener(tool);

        clearText = (Button)view.findViewById(R.id.clearText);
        clearText.setOnClickListener(tool);

        Log.i("NFCTool","Scanning");

        NfcManager manager;
        NfcAdapter adapter;

        manager = (NfcManager)getActivity().getSystemService(getActivity().getApplicationContext().NFC_SERVICE);
        adapter = manager.getDefaultAdapter();

        NFCCallback callback = new NFCCallback(getActivity());

        Bundle opts = new Bundle();
        opts.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 5000);

        adapter.enableReaderMode(getActivity(), callback, NfcAdapter.FLAG_READER_NFC_A, opts);

        Toast.makeText(this.getActivity(), "Please scan tag with device.", Toast.LENGTH_LONG).show();

        return view;
    }
}