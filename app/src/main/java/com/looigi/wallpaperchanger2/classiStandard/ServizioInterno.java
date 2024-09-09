package com.looigi.wallpaperchanger2.classiStandard;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import com.looigi.wallpaperchanger2.MainActivity;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

public class ServizioInterno extends Service {
    private static final String NomeMaschera = "SERVIZIOINTERNO";
    private Context context;
    private ScreenReceiver mScreenReceiver;
    private PowerManager.WakeLock wl;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Utility.getInstance().stopService(context);

        Utility.getInstance().ScriveLog(context, NomeMaschera, "onConfigurationChanged: " + newConfig.uiMode);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Start Command");

        return START_STICKY;
    }

    @SuppressLint({"ForegroundServiceType", "WakelockTimeout"})
    @Override
    public void onCreate() {
        context = this;
        VariabiliStaticheServizio.getInstance().setContext(this);

        Utility.getInstance().ScriveLog(context, NomeMaschera, "On Create");

        mScreenReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mScreenReceiver, filter);

        // CPU Attiva
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                VariabiliStaticheServizio.channelName);
        wl.acquire();
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Notification notifica = GestioneNotifiche.getInstance().StartNotifica(this);
        // VariabiliStatiche.getInstance().setNotifica(GestioneNotifiche.getInstance().StartNotifica(this));
        // if (VariabiliStatiche.getInstance().getNotifica() != null) {
        startForeground(VariabiliStaticheServizio.NOTIFICATION_CHANNEL_ID, notifica);
        Utility.getInstance().ScriveLog(context, NomeMaschera, "Notifica instanziata");
        GestioneNotifiche.getInstance().AggiornaNotifica("","");

        Esecuzione e = new Esecuzione(context);
        e.startServizio1();

        /* Toast.makeText(context, VariabiliStaticheServizio.channelName + ": Foreground Partito",
                Toast.LENGTH_SHORT).show();

        if (VariabiliStatiche.getInstance().getMainActivity() != null) {
            VariabiliStatiche.getInstance().getMainActivity().moveTaskToBack(false);
        }
        } else {
            Utility.getInstance().ScriveLog(context, NomeMaschera, "Notifica " + VariabiliStatiche.channelName + " nulla");
            Toast.makeText(this, "Notifica " + VariabiliStatiche.channelName + " nulla", Toast.LENGTH_SHORT).show();
        } */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Utility.getInstance().ScriveLog(context, NomeMaschera, "On Dest");

        wl.release();
        GestioneNotifiche.getInstance().RimuoviNotifica();
        unregisterReceiver(mScreenReceiver);
    }
}