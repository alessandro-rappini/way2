package com.example.alessandrorappini.way.Misurazioni.Misurazioni.NetWork;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.example.alessandrorappini.way.Interazione.AggiungiMisurazioni;
import com.example.alessandrorappini.way.Localizzazione.PrincipaleLocalizzati;

/**
 * Created by Alessandro Rappini on 03/12/2016.
 */

public  class NetWorkObjTask {
    static Context context;
    TelephonyManager telephonyManager;
    myPhoneStateListener psListener;
    String fine ;

    public NetWorkObjTask(Context c , String invio){
        fine = invio;
        context = c;
        inizia();
    }

    private void inizia() {
        psListener = new myPhoneStateListener();
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }



    public class myPhoneStateListener extends PhoneStateListener {
        public int signalStrengthValue;

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (signalStrength.isGsm()) {
                if (signalStrength.getGsmSignalStrength() != 99)
                    signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
                else
                    signalStrengthValue = signalStrength.getGsmSignalStrength();
            } else {
                signalStrengthValue = signalStrength.getCdmaDbm();
            }
            if(fine.equals("misurazioni")){
                AggiungiMisurazioni.inserisciCheifNetWord(signalStrengthValue);
            }

            if(fine.equals("localizzazione")){
                PrincipaleLocalizzati.inserisciCheifNetWordLocalizzazione(signalStrengthValue);
            }

            /*Log.i("inizio", "**************************");
            Log.i("info " , "Signal Strength : " + signalStrengthValue);
            Log.i("fine", "**************************");*/
        }

    }

    public void SetUnregisterReceiver() {
        //context.unregisterReceiver(telephonyManager);
        telephonyManager.listen(psListener, PhoneStateListener.LISTEN_NONE);

    }
}
