package com.example.alessandrorappini.way.Misurazioni.Misurazioni.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by Alessandro Rappini on 22/11/2016.
 */

public class BluetoothObjAsyncTask {

    int precisone , volteItenrato;
    Context con = null;
    Intent inte = null;

    private BluetoothAdapter mBluetoothAdapter;

    public BluetoothObjAsyncTask(int precisione , Context c, Intent i) {
        this.precisone = precisione;
        this.con = c;
        this.inte = i;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void esegui(){
        final BluetoothManager bluetoothManager = (BluetoothManager) con.getSystemService(con.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        mBluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    // get the discovered device as you wish
                    String nome = device.getAddress();
                    Log.i("info" , "-----");
                    Log.i("info" , nome);



                }
        });

        // rest of your code that will run **before** callback is triggered since it's asynchronous
    }




}

