package com.example.alessandrorappini.way.Misurazioni.Misurazioni.Bluetooth;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.alessandrorappini.way.Interazione.AggiungiMisurazioni;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothObj;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Alessandro Rappini on 22/11/2016.
 * https://github.com/kiteflo/iBeaconAndroidDemo/blob/master/app/src/main/java/com/sobag/beaconplayground/MainActivity.java
 * https://developer.android.com/samples/BluetoothLeGatt/src/com.example.android.bluetoothlegatt/DeviceScanActivity.html#l179
 * https://developer.android.com/guide/topics/connectivity/bluetooth-le.html
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BluetoothObjTask extends ListActivity {


    Context con = null;


    public BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private Handler scanHandler = new Handler();

    private static final long SCAN_PERIOD = 1000;
    private boolean isScanning = false;

    LinkedList<BluetoothObj> listBeaconsTemp;
    //HashMap <String , LinkedList> registro ;


    public BluetoothObjTask( Context c) {

        this.con = c;


        bluetoothManager = (BluetoothManager) con.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        listBeaconsTemp = new <HashMap>  LinkedList ();
        scanLeDevice(true);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            scanHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("info" ,  "STOP ------------------  DUE");
                    isScanning = false;
                    mBluetoothAdapter.stopLeScan(leScanCallback);
                    calcolaMediaOggetti();
                }
            }, SCAN_PERIOD);
            Log.i("info" ,  "START ------------------");
            isScanning = true;
            mBluetoothAdapter.startLeScan(leScanCallback);
        } else {
            Log.i("info" ,  "STOP ------------------   UNO");
            isScanning = false;
            mBluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    protected BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback()
    {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord)
        {

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

                Log.i("!!!!!!" , "RILEVATO:");
                Log.i("info" ,"UUID: " +uuid + "\\nmajor: " +major +"\\nminor" +minor);
                Log.i("device " , device + "");
                Log.i("rssi" ,  rssi + "");

                if ( listBeaconsTemp.size() == 0){
                    BluetoothObj bluetoothObj = new BluetoothObj(device.toString());
                    bluetoothObj.inserisciRssi(rssi);
                    listBeaconsTemp.add(bluetoothObj);
                }else {
                    Boolean eisteinza = controllaEsistenza(device.toString());
                    if (eisteinza==false){
                        // abbiamo trovato un nuovo device
                        BluetoothObj bluetoothObj = new BluetoothObj(device.toString());
                        bluetoothObj.inserisciRssi(rssi);
                        listBeaconsTemp.add(bluetoothObj);
                    }else {
                        // il device esiste già, dobbiamo aggiungere i valori
                        BluetoothObj oggettoDaAggiorare = passamiOggettoDaAggiorare(device.toString());
                        oggettoDaAggiorare.inserisciRssi(rssi);
                    }
                }
            }
        }
    };

    private BluetoothObj passamiOggettoDaAggiorare(String device) {
        BluetoothObj oggettoDaRitornare = null;
        for (int i=0 ; i < listBeaconsTemp.size() ; i++) {
            BluetoothObj appoggioBluetoothObj = listBeaconsTemp.get(i);
            if (appoggioBluetoothObj.getDevice().equals(device)){
                oggettoDaRitornare = appoggioBluetoothObj;
            }
        }
        return oggettoDaRitornare;
    }

    private Boolean controllaEsistenza(String device) {
        Boolean esiste = false;
        for (int i=0 ; i < listBeaconsTemp.size() ; i++) {
            BluetoothObj appoggioBluetoothObj = listBeaconsTemp.get(i);
            if (appoggioBluetoothObj.getDevice().equals(device)){
                esiste = true;
            }
        }
        return esiste;
    }

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

    private void calcolaMediaOggetti() {
        for (int i=0 ; i < listBeaconsTemp.size() ; i++) {
            BluetoothObj appoggioBluetoothObj = listBeaconsTemp.get(i);
            // non lo si usa piu
            //appoggioBluetoothObj.calcolaMediaRssi();

            // non pulisco la lista dopo
            appoggioBluetoothObj.prendiIlPrimo();
        }
        AggiungiMisurazioni.inserisciCheifBlue(listBeaconsTemp);
    }


}

