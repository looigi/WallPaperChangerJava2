package com.looigi.wallpaperchanger2.classiPlayer;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

public class HeadsetBroadcastReceiver extends BroadcastReceiver {
    private int StavaSuonando = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            //Device found
            // Toast.makeText(context, "device found", Toast.LENGTH_LONG).show();
        } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            //Device is now connected
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            String nome = device.getName();
            int tipo = device.getType();

            if (StavaSuonando == 1) {
                UtilityPlayer.getInstance().PressionePlay(context, false);
                StavaSuonando = -1;
            }
            if (VariabiliStatichePlayer.getInstance().getImgCuffie() != null) {
                VariabiliStatichePlayer.getInstance().getImgCuffie().setVisibility(LinearLayout.VISIBLE);
            }
            UtilityPlayer.getInstance().ScriveLog(context, "CUFFIEINS", "Gestione inserimento cuffie: Cuffie Inserite");

            UtilitiesGlobali.getInstance().ApreToast(context, "Cuffie inserite: " + nome + " Tipo: " + tipo);
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            //Done searching
            // Toast.makeText(getApplicationContext(), "search ended", Toast.LENGTH_LONG).show();
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
            //Device is about to disconnect
            // Toast.makeText(getApplicationContext(), "device is about to disconnect", Toast.LENGTH_LONG).show();
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            //Device has disconnected
            if (device != null) {
                String nome = device.getName();
                int tipo = device.getType();

                if (VariabiliStatichePlayer.getInstance().getImgCuffie() != null) {
                    VariabiliStatichePlayer.getInstance().getImgCuffie().setVisibility(LinearLayout.GONE);
                }
                UtilityPlayer.getInstance().ScriveLog(context, "CUFFIEINS", "Gestione inserimento cuffie: Cuffie Disinserite");

                if (VariabiliStatichePlayer.getInstance().isStaSuonando()) {
                    StavaSuonando = 1;
                    UtilityPlayer.getInstance().PressionePlay(context, false);
                } else {
                    StavaSuonando = -1;
                }

                UtilitiesGlobali.getInstance().ApreToast(context, "Cuffie disinserite: " + nome + " Tipo: " + tipo);
            }
        }
    }
}