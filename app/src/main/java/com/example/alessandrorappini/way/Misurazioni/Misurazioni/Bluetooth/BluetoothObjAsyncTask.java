package com.example.alessandrorappini.way.Misurazioni.Misurazioni.Bluetooth;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by Alessandro Rappini on 22/11/2016.
 * https://github.com/kiteflo/iBeaconAndroidDemo/blob/master/app/src/main/java/com/sobag/beaconplayground/MainActivity.java
 * https://developer.android.com/samples/BluetoothLeGatt/src/com.example.android.bluetoothlegatt/DeviceScanActivity.html#l179
 * https://developer.android.com/guide/topics/connectivity/bluetooth-le.html
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BluetoothObjAsyncTask extends ListActivity {

    int precisone;
    Context con = null;
    Intent inte = null;


    public BluetoothManager bluetoothManager;

    private BluetoothAdapter mBluetoothAdapter;

    private Handler scanHandler = new Handler();
    private int scan_interval_ms = 5000;

    private static final long SCAN_PERIOD = 15000;
    private boolean isScanning = false;


    public BluetoothObjAsyncTask(int precisione, Context c, Intent i) {
        this.precisone = precisione;
        this.con = c;
        this.inte = i;

        bluetoothManager = (BluetoothManager) con.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        scanLeDevice(true);
     //   scanHandler.post(scanRunnable);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            scanHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isScanning = false;
                    mBluetoothAdapter.stopLeScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            isScanning = true;
            mBluetoothAdapter.startLeScan(leScanCallback);
        } else {
            isScanning = false;
            mBluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    /*private Runnable scanRunnable = new Runnable()
    {
        @Override
        public void run() {
            if (isScanning) {
                if (mBluetoothAdapter != null)
                {
                    Log.i("info " , " stop le scan ------------------------------------ STOP");
                    mBluetoothAdapter.stopLeScan(leScanCallback);
                }
            }
            else
            {
                if (mBluetoothAdapter != null)
                {
                    Log.i("info " , " Start le scan  ------------------------------------ START");
                    mBluetoothAdapter.startLeScan(leScanCallback);
                }
            }
            isScanning = !isScanning;
            scanHandler.postDelayed(this, scan_interval_ms);
        }
    };*/


    protected BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback()
    {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord)
        {
            Log.i("!!!!!!" , "entrp dentro BluetoothAdapter");
            Log.i("device " , device + "");
            Log.i("rssi" ,  rssi + "");
            Log.i("info" , "fermo il tutto");



            int startByte = 2;
            boolean patternFound = false;
            while (startByte <= 5)
            {
                if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15)
                { //Identifies correct data length
                    patternFound = true;
                    break;
                }
                startByte++;
            }

            if (patternFound)
            {
                //Convert to hex String
                byte[] uuidBytes = new byte[16];
                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                String hexString = bytesToHex(uuidBytes);

                //UUID detection
                String uuid =  hexString.substring(0,8) + "-" +
                        hexString.substring(8,12) + "-" +
                        hexString.substring(12,16) + "-" +
                        hexString.substring(16,20) + "-" +
                        hexString.substring(20,32);
                // major
                final int major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);
                // minor
                final int minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);
                Log.i("info" ,"UUID: " +uuid + "\\nmajor: " +major +"\\nminor" +minor);
            }
        }
    };

    /**
     * bytesToHex method
     */
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


}

