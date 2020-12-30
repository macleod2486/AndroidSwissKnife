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
package com.macleod2486.androidswissknife.views

import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.macleod2486.androidswissknife.R
import com.macleod2486.androidswissknife.components.NFCCallback
import com.macleod2486.androidswissknife.components.NFCTool

class NFC : Fragment() {
    var writeNFC: Button? = null
    var clearText: Button? = null
    var tool: NFCTool? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.nfc, container, false)
        tool = NFCTool(activity)
        writeNFC = view.findViewById<View>(R.id.writeNFC) as Button
        writeNFC!!.setOnClickListener(tool)
        clearText = view.findViewById<View>(R.id.clearText) as Button
        clearText!!.setOnClickListener(tool)
        Log.i("NFCTool", "Scanning")
        val manager: NfcManager
        val adapter: NfcAdapter
        manager = activity!!.getSystemService(Context.NFC_SERVICE) as NfcManager
        adapter = manager.defaultAdapter
        val callback = NFCCallback(activity)
        val opts = Bundle()
        opts.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 5000)
        adapter.enableReaderMode(activity, callback, NfcAdapter.FLAG_READER_NFC_A, opts)
        Toast.makeText(this.activity, "Please scan tag with device.", Toast.LENGTH_LONG).show()
        return view
    }
}