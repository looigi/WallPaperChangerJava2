package com.looigi.wallpaperchanger2.classeAntifurto;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classePreview.MainPreview;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UtilityAntifurto {
    private static final String NomeMaschera = "Utilities_Antifurto";
    private static UtilityAntifurto instance = null;
    private String[] App = new String[0];
    private String[] paths = new String[0];

    private UtilityAntifurto() {
    }

    public static UtilityAntifurto getInstance() {
        if (instance == null) {
            instance = new UtilityAntifurto();
        }

        return instance;
    }

    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;
    private final int secondiAttesa = 5000;
    private int quantiBTPrima = -1;

    public void AttivaAntifurto(Context context, boolean Attivato) {
        if (Attivato) {
            if (!VariabiliStaticheAntifurto.getInstance().isAllarmeAttivo()) {
                VariabiliStaticheAntifurto.getInstance().setAllarmeInCorso(false);
                VariabiliStaticheAntifurto.getInstance().setAllarmeAttivo(true);

                context.startService(new Intent(context, ShakeService.class));

                FermaTimer();

                handlerThread = new HandlerThread("background-thread_Antifurto_" +
                        VariabiliStaticheWallpaper.channelName);
                handlerThread.start();

                handler = new Handler(handlerThread.getLooper());
                r = new Runnable() {
                    public void run() {
                        if (VariabiliStaticheAntifurto.getInstance().isAllarmeSuBT()) {
                            if (!VariabiliStaticheAntifurto.getInstance().isAllarmeInCorso()) {
                                getConnectedBLEDevices(context);
                            }
                        } else {
                            if (VariabiliStaticheAntifurto.getInstance().getTxtInfo2() != null) {
                                Handler handlerTimer = new Handler(Looper.getMainLooper());
                                Runnable rTimer = new Runnable() {
                                    public void run() {
                                        VariabiliStaticheAntifurto.getInstance().getTxtInfo2().setText("BT Disabilitato");
                                    }
                                };
                                handlerTimer.postDelayed(rTimer, 100);
                            }

                        }

                        if (handler != null) {
                            handler.postDelayed(this, secondiAttesa);
                        }
                    }
                };
                handler.postDelayed(r, secondiAttesa);
            }
        } else {
            if (VariabiliStaticheAntifurto.getInstance().isAllarmeAttivo()) {
                FermaTimer();

                VariabiliStaticheAntifurto.getInstance().getTxtAllarme().setText("");

                // VariabiliStaticheAntifurto.getInstance().getShakeDetector().stop();
                context.stopService(new Intent(context, ShakeService.class));

                VariabiliStaticheAntifurto.getInstance().setAllarmeInCorso(false);
                VariabiliStaticheAntifurto.getInstance().setAllarmeAttivo(false);

                if (VariabiliStaticheAntifurto.getInstance().getActAllarme() != null) {
                    VariabiliStaticheAntifurto.getInstance().getActAllarme().finish();
                }
            }
        }

        GestioneNotificheTasti.getInstance().AggiornaNotifica();
    }

    public void FermaTimer() {
        if (handler != null && r != null && handlerThread != null) {
            // handlerThread.quit();

            handler.removeCallbacksAndMessages(null);

            handler.removeCallbacks(r);
            handler = null;
            r = null;
        }
    }

    private void getConnectedBLEDevices(Context context) {
            List<String> devices = getAllConnectedDevices(context);

            int quantiBT = devices.size();
            String ListaBT = "";


            if (VariabiliStaticheAntifurto.getInstance().getTxtInfo2() != null) {
                for (String bt: devices) {
                    ListaBT += bt + "\n";
                }

                Handler handlerTimer = new Handler(Looper.getMainLooper());
                String finalListaBT = ListaBT;
                Runnable rTimer = new Runnable() {
                    public void run() {
                        VariabiliStaticheAntifurto.getInstance().getTxtInfo2().setText(finalListaBT);
                    }
                };
                handlerTimer.postDelayed(rTimer, 100);
            }

            if (quantiBT < quantiBTPrima && quantiBTPrima > -1) {
                quantiBTPrima = quantiBT;

                if (!VariabiliStaticheAntifurto.getInstance().isAllarmeInCorso()) {
                    boolean Ok = false;

                    String btMonitorato = VariabiliStaticheAntifurto.getInstance().getBtMonitorato();
                    if (btMonitorato.isEmpty()) {
                        Ok = true;
                    } else {
                        if (!ListaBT.toUpperCase().contains(btMonitorato.toUpperCase().trim())) {
                            Ok = true;
                        }
                    }

                    if (Ok) {
                        VariabiliStaticheAntifurto.getInstance().setAllarmeInCorso(true);
                        VariabiliStaticheAntifurto.getInstance().getTxtAllarme().setText("BLUETOOTH SCOLLEGATO");

                        Intent i = new Intent(context, MainAntifurto.class);
                        i.putExtra("DO", "ALLARMEBT");
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }
            }
    }

    public static List<String> getAllConnectedDevices(Context context) {
        List<String> connectedDevices = new ArrayList<>();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            connectedDevices.add("Bluetooth non supportato");
            return connectedDevices;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            connectedDevices.add("Permessi mancanti (BLUETOOTH_CONNECT)");
            return connectedDevices;
        }

        // Evita duplicati
        Set<String> uniqueDevices = new HashSet<>();

        // 1) Classici: A2DP
        bluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                List<BluetoothDevice> devices = proxy.getConnectedDevices();
                for (BluetoothDevice device : devices) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    uniqueDevices.add("A2DP: " + device.getName() + " - " + device.getAddress());
                }
                bluetoothAdapter.closeProfileProxy(profile, proxy);
            }

            @Override
            public void onServiceDisconnected(int profile) {}
        }, BluetoothProfile.A2DP);

        // 2) Classici: HEADSET
        bluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                List<BluetoothDevice> devices = proxy.getConnectedDevices();
                for (BluetoothDevice device : devices) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    uniqueDevices.add("HEADSET: " + device.getName() + " - " + device.getAddress());
                }
                bluetoothAdapter.closeProfileProxy(profile, proxy);
            }

            @Override
            public void onServiceDisconnected(int profile) {}
        }, BluetoothProfile.HEADSET);

        // 3) BLE: GATT
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            List<BluetoothDevice> bleDevices = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);
            for (BluetoothDevice device : bleDevices) {
                uniqueDevices.add("BLE: " + device.getName() + " - " + device.getAddress());
            }
        }

        if (uniqueDevices.isEmpty()) {
            connectedDevices.add("Nessun dispositivo connesso");
        } else {
            connectedDevices.addAll(uniqueDevices);
        }

        return connectedDevices;
    }
}
