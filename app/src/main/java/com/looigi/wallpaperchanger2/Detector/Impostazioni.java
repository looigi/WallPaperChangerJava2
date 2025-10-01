package com.looigi.wallpaperchanger2.Detector;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.EditText;

public class Impostazioni {
    private static final String NomeMaschera = "Impostazioni";

    /* public void PrendeRisoluzioni(Context context) {
        GBTakePictureNoPreview c = new GBTakePictureNoPreview();
        c.ImpostaContext(context);
        if (VariabiliStatiche.getInstance().getFotocamera()==1) {
            c.setUseFrontCamera();
        } else {
            c.setUseBackCamera();
        }
        VariabiliStatiche.getInstance().Dimensioni=c.RitornaRisoluzioni();
        RiempieListaRisoluzioni(context, VariabiliStatiche.getInstance().Dimensioni);
    }

    public void RiempieListaRisoluzioni(Context context, List<String> Risoluzioni) {
        String sRisoluzioni[] = null;
        sRisoluzioni = new String[Risoluzioni.size()];
        for (int i = 0; i < Risoluzioni.size(); i++) {
            sRisoluzioni[i] = Risoluzioni.get(i);
        }

        ArrayList<String> ListaRis=new ArrayList<String>();
        String[] risol = {};
        risol = new String[sRisoluzioni.length];
        for (int i = 0;i < sRisoluzioni.length; i++) {
            risol[i] = new String(sRisoluzioni[i]);
            ListaRis.add(risol[i]);
        }

        ArrayList<HashMap<String, Object>>
                data = new ArrayList<HashMap<String,Object>>();

        HashMap<String,Object>
                ListaFinale = new HashMap<String, Object>();
        for (int i = 0; i < ListaRis.size(); i++){
            String p = ListaRis.get(i);

            ListaFinale = new HashMap<String, Object>();
            ListaFinale.put("Risoluzione", p);

            data.add(ListaFinale);
        }
        /* String[] from = {"Risoluzione"};
        int[] to = {R.id.Risoluzione};
        SimpleAdapter adapter=new SimpleAdapter(
                context.getApplicationContext(),
                data,
                R.layout.listview_records,
                from,
                to); * /

        // VariabiliStaticheDetector.getInstance().getLista().setAdapter(adapter);
    } */

    private void SalvaValori(Context context) {
        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Scrittura impostazioni");

        db_dati_detector db = new db_dati_detector(context);
        db.ScriveImpostazioni(context, "SALVA VALORI");
        db.ChiudeDB();
        // DBImpostazioni dbImpostazioni = new DBImpostazioni(VariabiliStatiche.getInstance().getContext());
        // dbImpostazioni.open();
        // dbImpostazioni.ScriveImpostazioni();
        // dbImpostazioni.close();
    }

    public void ImpostaEstensioneOriginale(Context context) {
        // SQLiteDatabase myDB= null;
//
        // myDB = context.openOrCreateDatabase("Spiator", MODE_PRIVATE, null);
        // myDB.execSQL("Update Modalita Set Estensione=1");
        // myDB.close();
        VariabiliStaticheDetector.getInstance().setEstensione(1);
        SalvaValori(context);
    }

    public void ImpostaEstensioneMascherata(Context context) {
        // SQLiteDatabase myDB= null;
//
        // myDB = context.openOrCreateDatabase("Spiator", MODE_PRIVATE, null);
        // myDB.execSQL("Update Modalita Set Estensione=2");
        // myDB.close();
        VariabiliStaticheDetector.getInstance().setEstensione(2);
        SalvaValori(context);
    }

    /* public void ImpostaRisoluzione(Context context, String Risol) {
        // SQLiteDatabase myDB= null;
//
        // myDB = context.openOrCreateDatabase("Spiator", MODE_PRIVATE, null);
        // myDB.execSQL("Update Modalita Set Risoluzione='"+Risol+"'");
        // myDB.close();
        VariabiliStaticheDetector.getInstance().setRisoluzione(Risol);
        SalvaValori(context);
    } */

    /* public void ImpostaFrontale(Context context, TextView tv) {
        // SQLiteDatabase myDB= null;
//
        // myDB = context.openOrCreateDatabase("Spiator", MODE_PRIVATE, null);
        // myDB.execSQL("Update Modalita Set Fotocamera=1");
        VariabiliStatiche.getInstance().setFotocamera(1);
        // myDB.close();

        GBTakePictureNoPreview c = new GBTakePictureNoPreview();
        c.ImpostaContext(context);
        c.setUseFrontCamera();
        VariabiliStatiche.getInstance().Dimensioni=c.RitornaRisoluzioni();
        String risol = VariabiliStatiche.getInstance().RitornaRisoluzioneMassima(VariabiliStatiche.getInstance().Dimensioni);

        ImpostaRisoluzione(context, risol);
        tv.setText(risol);
        PrendeRisoluzioni(context);
        SalvaValori();
    }

    public void ImpostaRetro(Context context, TextView tv) {
        // SQLiteDatabase myDB= null;

        // myDB = context.openOrCreateDatabase("Spiator", MODE_PRIVATE, null);
        // myDB.execSQL("Update Modalita Set Fotocamera=2");
        VariabiliStatiche.getInstance().setFotocamera(2);
        // myDB.close();

        GBTakePictureNoPreview c = new GBTakePictureNoPreview();
        c.ImpostaContext(context);
        c.setUseBackCamera();
        VariabiliStatiche.getInstance().Dimensioni=c.RitornaRisoluzioni();
        String risol = VariabiliStatiche.getInstance().RitornaRisoluzioneMassima(VariabiliStatiche.getInstance().Dimensioni);

        ImpostaRisoluzione(context, risol);
        tv.setText(risol);
        PrendeRisoluzioni(context);
        SalvaValori();
    } */

    public void ImpostaAutoScatto(Context context, EditText et) {
        // SQLiteDatabase myDB= null;

        if (et != null) {
            String Secondi = et.getText().toString();

            // myDB = context.openOrCreateDatabase("Spiator", MODE_PRIVATE, null);
            // myDB.execSQL("Update Modalita Set Valore=1, Secondi="+Secondi);
            // myDB.close();

            if (!Secondi.isEmpty()) {
                VariabiliStaticheDetector.getInstance().setTipologiaScatto(1);
                VariabiliStaticheDetector.getInstance().setSecondi(Integer.parseInt(Secondi));
                SalvaValori(context);
            }
        }
    }

    public void ImpostaImmediato(Context context) {
        // SQLiteDatabase myDB= null;
//
        // myDB = context.openOrCreateDatabase("Spiator", MODE_PRIVATE, null);
        // myDB.execSQL("Update Modalita Set Valore=2");
        // myDB.close();
        VariabiliStaticheDetector.getInstance().setTipologiaScatto(2);
        SalvaValori(context);
    }

    public void ImpostaVibrazione(Context context, boolean Attivo) {
        /* String Vibr="";

        if (Attivo) {
            Vibr="S";
        } else {
            Vibr="N";
        } */

        // SQLiteDatabase myDB= null;
//
        // myDB = context.openOrCreateDatabase("Spiator", MODE_PRIVATE, null);
        // myDB.execSQL("Update Vibrazione Set Vibraz='"+Vibr+"';");
        // myDB.close();

        VariabiliStaticheDetector.getInstance().setVibrazione(Attivo);
        SalvaValori(context);
    }

    public void ImpostaVisualizzaToast(Context context, boolean Attivo) {
        /* String vT="";

        if (Attivo) {
            vT="S";
        } else {
            vT="N";
        } */

        // SQLiteDatabase myDB= null;
//
        // myDB = context.openOrCreateDatabase("Spiator", MODE_PRIVATE, null);
        // myDB.execSQL("Update Vibrazione Set Vibraz='"+Vibr+"';");
        // myDB.close();

        VariabiliStaticheDetector.getInstance().setVisualizzaToast(Attivo);
        SalvaValori(context);
    }

    public void ImpostaAnteprima(Context context, CheckBox c) {
        String Ant="";

        if (c.isChecked()) {
            Ant="S";
        } else {
            Ant="N";
        }

        // SQLiteDatabase myDB= null;
//
        // myDB = context.openOrCreateDatabase("Spiator", MODE_PRIVATE, null);
        // myDB.execSQL("Update Anteprima Set Ant='"+Ant+"';");
        // myDB.close();

        VariabiliStaticheDetector.getInstance().setAnteprima(Ant);
        SalvaValori(context);
    }

    public void ImpostaNumScatti(Context context, EditText t) {
        int Scatti;

        String s=t.getText().toString();
        Scatti=Integer.parseInt(s);

        // SQLiteDatabase myDB= null;
//
        // myDB = context.openOrCreateDatabase("Spiator", MODE_PRIVATE, null);
        // myDB.execSQL("Update NumeroScatti Set Numero="+Scatti+";");
        // myDB.close();

        VariabiliStaticheDetector.getInstance().setNumeroScatti(Scatti);
        SalvaValori(context);
    }
}
