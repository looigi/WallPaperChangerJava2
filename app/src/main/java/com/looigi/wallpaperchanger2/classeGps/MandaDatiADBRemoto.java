package com.looigi.wallpaperchanger2.classeGps;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classeModificheCodice.webService.ChiamateWSModifiche;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.utilities.ClasseZip;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MandaDatiADBRemoto {
    private static final String NomeMaschera = "MandaDatiADBRemoto";

    public void inviaDatiPresentiSulDB(Context context, boolean EseguePulizia, boolean SoloGiornoAttuale,
                                       String DataVisualizzata) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                UtilityGPS.getInstance().ImpostaAttesa(true);

                db_dati_gps db = new db_dati_gps(context);
                List<String> listaDate = new ArrayList<>();
                if (SoloGiornoAttuale) {
                    // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    // String sDataAttuale = sdf.format(new Date());

                    listaDate.add(DataVisualizzata);
                } else {
                    listaDate = db.RitornaDatePresenti();
                }

                String Cartella = context.getFilesDir() + "/DatiGPS";
                Files.getInstance().CreaCartelle(context.getFilesDir() + "/DatiGPS");

                List<String> FilesZip = new ArrayList<>();
                boolean trovataRoba = false;

                for (String data: listaDate) {
                    String Dati = db.EstraiPosizioni(data, false);
                    if (!Dati.isEmpty()) {
                        String[] s = data.split("/");
                        String sDataAttuale = s[2] + "-" + s[1] + "-" + s[0];
                        String SoloNome = "DatiGPS_" + sDataAttuale;
                        Files.getInstance().EliminaFileUnico(Cartella + "/" + SoloNome + ".csv");
                        Files.getInstance().ScriveFile(
                                Cartella,
                                SoloNome + ".csv",
                                Dati);

                        if (Files.getInstance().EsisteFile(Cartella + "/" + SoloNome + ".csv")) {
                            FilesZip.add(Cartella + "/" + SoloNome + ".csv");
                            trovataRoba = true;

                            if (EseguePulizia) {
                                db.EliminaPosizioni(data);
                            }
                        } else {
                            // File CSV non creato
                        }
                    }
                }

                if (trovataRoba) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String sDataAttuale = sdf.format(new Date());

                    Files.getInstance().EliminaFileUnico(Cartella + "/DatiGPS_" + sDataAttuale + ".zip");

                    List<String> lfz = new ArrayList<>();
                    for (String filetto : FilesZip) {
                        lfz.add(filetto);
                    }

                    ClasseZip z = new ClasseZip();
                    z.ZippaFile(context, Cartella + "/", lfz, "DatiGPS_" + sDataAttuale);

                    for (String filetto : FilesZip) {
                        Files.getInstance().EliminaFileUnico(filetto);
                    }

                    UtilityGPS.getInstance().ImpostaAttesa(false);

                    ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                    ws.Esporta("GPS", Cartella + "/DatiGPS_" + sDataAttuale + ".zip");
                } else {
                    UtilityGPS.getInstance().ImpostaAttesa(false);
                    UtilitiesGlobali.getInstance().ApreToast(context, "Nessuna posizione rilevata");
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                    }
                });
            }
        });
    }
}
