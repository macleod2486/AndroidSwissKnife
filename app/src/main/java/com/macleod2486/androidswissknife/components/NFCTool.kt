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
package com.macleod2486.androidswissknife.components

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.nfc.tech.Ndef
import androidx.fragment.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.macleod2486.androidswissknife.R

class NFCTool(var activity: FragmentActivity) : View.OnClickListener {
    lateinit var manager: NfcManager
    lateinit var adapter: NfcAdapter
    lateinit var entryText: EditText
    override fun onClick(view: View) {
        Log.i("NFCTool", "Clicked")
        manager = activity.getSystemService(Context.NFC_SERVICE) as NfcManager
        adapter = manager.defaultAdapter
        if (view.id == R.id.writeNFC) {
            adapter.disableForegroundDispatch(activity)
            Log.i("NFCTool", "Writing")
            write()
        }
        if (view.id == R.id.clearText) {
            Log.i("NFCTool", "Clearing text")
            entryText = activity.findViewById<View>(R.id.textEntry) as EditText
            entryText.setText("")
        }
    }

    private fun write() {
        Log.i("NFCTool", "Write")
        entryText = activity.findViewById<View>(R.id.textEntry) as EditText
        setUpWrite(entryText.text.toString())
    }

    private fun setUpWrite(message: String) {
        Log.i("NFCTool", "Message received $message")
        val nfcIntent = Intent(activity.applicationContext, NFCActivity::class.java)
        nfcIntent.putExtra("NFCMode", "write")
        nfcIntent.putExtra("NFCMessage", message)
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(activity, 0, nfcIntent, 0)
        val filter = IntentFilter()
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED)
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED)
        val filterArray = arrayOf(filter)
        val techListsArray = arrayOf(arrayOf(Ndef::class.java.name), arrayOf(Ndef::class.java.name))
        adapter.disableReaderMode(activity)
        adapter.enableForegroundDispatch(activity, pendingIntent, filterArray, techListsArray)
        Toast.makeText(activity, "Please scan tag with device.", Toast.LENGTH_LONG).show()
    }
}