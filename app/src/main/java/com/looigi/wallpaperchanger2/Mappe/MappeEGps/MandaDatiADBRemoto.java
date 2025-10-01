package com.looigi.wallpaperchanger2.Mappe.MappeEGps;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.ModificheCodice.webService.ChiamateWSModifiche;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.UtilitiesVarie.ClasseZip;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
                String listaDateDaEliminare = "";
                String DataDaSalvare = "";

                for (String data: listaDate) {
                    String Dati = db.EstraiPosizioni(data, false);
                    if (!Dati.isEmpty()) {
                        String[] s = data.split("/");
                        String sDataAttuale = s[2] + "-" + s[1] + "-" + s[0];
                        DataDaSalvare = sDataAttuale;
                        String SoloNome = "DatiGPS_" + sDataAttuale;
                        Files.getInstance().EliminaFileUnico(Cartella + "/" + SoloNome + ".csv");
                        Files.getInstance().ScriveFile(
                                Cartella,
                                SoloNome + ".csv",
                                Dati);

                        if (Files.getInstance().EsisteFile(Cartella + "/" + SoloNome + ".csv")) {
                            FilesZip.add(Cartella + "/" + SoloNome + ".csv");
                            trovataRoba = true;

                            // if (EseguePulizia) {
                            //     db.EliminaPosizioni(data);
                            // }
                            listaDateDaEliminare += data + ";";
                        } else {
                            // File CSV non creato
                        }
                    }
                }

                Calendar calendar = Calendar.getInstance();
                int seconds = calendar.get(Calendar.SECOND);
                if (!SoloGiornoAttuale) {
                    int day = calendar.get(Calendar.DAY_OF_WEEK);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);
                    int hour = calendar.get(Calendar.HOUR);
                    int minute = calendar.get(Calendar.MINUTE);
                    DataDaSalvare = "TuttoIlDB_" + year + "_" + month + "_" + day + "_" + hour + "_" + minute + "_" + seconds;
                } else {
                    DataDaSalvare += " (" + seconds + ")";
                }

                if (trovataRoba) {
                    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    // String sDataAttuale = sdf.format(new Date());

                    Files.getInstance().EliminaFileUnico(Cartella + "/DatiGPS_" + DataDaSalvare + ".zip");

                    List<String> lfz = new ArrayList<>();
                    for (String filetto : FilesZip) {
                        lfz.add(filetto);
                    }

                    ClasseZip z = new ClasseZip();
                    z.ZippaFile(context, Cartella + "/", lfz, "DatiGPS_" + DataDaSalvare);

                    for (String filetto : FilesZip) {
                        Files.getInstance().EliminaFileUnico(filetto);
                    }

                    UtilityGPS.getInstance().ImpostaAttesa(false);

                    ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                    ws.Esporta("GPS", Cartella + "/DatiGPS_" + DataDaSalvare + ".zip", EseguePulizia, listaDateDaEliminare);
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
