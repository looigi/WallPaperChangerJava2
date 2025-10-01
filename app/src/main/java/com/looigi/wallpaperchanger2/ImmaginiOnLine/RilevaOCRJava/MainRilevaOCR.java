package com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.webService.ChiamateWSRilevaOCR;

public class MainRilevaOCR extends Activity {
    private Context context;
    private Activity act = this;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_rileva_ocr);

        context = this;
        act = this;

        if (!VariabiliStaticheRilevaOCRJava.getInstance().isGiaEntrato()) {
            VariabiliStaticheRilevaOCRJava.getInstance().setContext(this);
            VariabiliStaticheRilevaOCRJava.getInstance().setContatore(0);

            stoChiudendo = false;

            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::CpuLock");
            wakeLock.acquire();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        VariabiliStaticheRilevaOCRJava.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoOCR));
        if (!VariabiliStaticheRilevaOCRJava.getInstance().isGiaEntrato()) {
            UtilitiesRilevaOCRJava.getInstance().Attesa(false);
        } else {
            if (VariabiliStaticheRilevaOCRJava.getInstance().isStaElaborando()) {
                UtilitiesRilevaOCRJava.getInstance().Attesa(true);
            } else {
                UtilitiesRilevaOCRJava.getInstance().Attesa(false);
            }
        }

        VariabiliStaticheRilevaOCRJava.getInstance().setImgImmagine(findViewById(R.id.imgImmagine));
        if (VariabiliStaticheRilevaOCRJava.getInstance().isGiaEntrato()) {
            UtilitiesRilevaOCRJava.getInstance().DisegnaImmagine(context);
        }

        if (!VariabiliStaticheRilevaOCRJava.getInstance().isGiaEntrato()) {
            VariabiliStaticheRilevaOCRJava.getInstance().setStaElaborando(true);
        }

        ChiamateWSRilevaOCR ws = new ChiamateWSRilevaOCR(context);
        Button btnFerma = (findViewById(R.id.btnFerma));
        if (!VariabiliStaticheRilevaOCRJava.getInstance().isGiaEntrato()) {
            btnFerma.setText("Ferma");
        }
        btnFerma.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheRilevaOCRJava.getInstance().isStaElaborando()) {
                    VariabiliStaticheRilevaOCRJava.getInstance().setStaElaborando(false);
                    btnFerma.setText("Parti");
                } else {
                    VariabiliStaticheRilevaOCRJava.getInstance().setStaElaborando(true);
                    btnFerma.setText("Ferma");

                    ws.RitornaProssimaImmagineDaLeggereInJava();
                }
            }
        });
        VariabiliStaticheRilevaOCRJava.getInstance().setTxtAvanzamento(findViewById(R.id.txtAvanzamento));

        if (!VariabiliStaticheRilevaOCRJava.getInstance().isGiaEntrato()) {
            VariabiliStaticheRilevaOCRJava.getInstance().setGiaEntrato(true);

            ws.RitornaProssimaImmagineDaLeggereInJava();
        }
    }

    private static final int NOTIFICATION_ID = 14789;
    private static final String CHANNEL_ID = "MainRilevaOCR";
    private boolean stoChiudendo = false;

    @Override
    protected void onStop() {
        super.onStop();

        if (!stoChiudendo) {
            // Mostra la notifica quando l’activity va in background
            showNotification();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!stoChiudendo) {
            GestioneNotificheOCR.getInstance().RimuoviNotifica();

            /* NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID); */
        }
    }

    Notification notifica;

    private void showNotification() {
        notifica = GestioneNotificheOCR.getInstance().StartNotifica(this);
        // VariabiliStatiche.getInstance().setNotifica(GestioneNotifiche.getInstance().StartNotifica(this));
        if (notifica != null) {
            GestioneNotificheOCR.getInstance().AggiornaNotifica();
        }

        /* NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelName = "OCR Channel";

        // Per Android 8.0+ serve il NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Intent per riaprire l'activity cliccando la notifica
        Intent intent = new Intent(this, MainRilevaOCR.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.audio) // tua icona in drawable
                .setContentTitle("OCR in background")
                .setSilent(true)
                .setContentText("Hai premuto Home, l’ocr è ancora attivo.")
                .setAutoCancel(true) // la notifica sparisce se l’utente la tocca
                .setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build()); */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        /* VariabiliStaticheRilevaOCRJava.getInstance().setStaElaborando(false);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        act.finish(); */
        if (!VariabiliStaticheRilevaOCRJava.getInstance().isStaElaborando()) {
            stoChiudendo = true;
            GestioneNotificheOCR.getInstance().RimuoviNotifica();
            VariabiliStaticheRilevaOCRJava.getInstance().setGiaEntrato(false);
            act.finish();
        // } else {
        //     showNotification();
        }
    }

    /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                VariabiliStaticheRilevaOCRJava.getInstance().setStaElaborando(false);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                if (wakeLock != null && wakeLock.isHeld()) {
                    wakeLock.release();
                }
                this.finish();

                return true;
        }

        return false;
    } */
}
