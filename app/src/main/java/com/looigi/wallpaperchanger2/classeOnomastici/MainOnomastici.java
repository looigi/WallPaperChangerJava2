package com.looigi.wallpaperchanger2.classeOnomastici;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeOnomastici.strutture.CampiRitornoSanti;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainOnomastici extends Activity implements ColorPickerDialog.OnColorChangedListener {
    private List<String> nomiRilevati=new ArrayList<String>();
    private List<String> nomiRilevatiNormale=new ArrayList<String>();
    private List<String> nomiSelezionati=new ArrayList<String>();
    private List<String> listaMessaggi=new ArrayList<String>();
    private boolean PulisceDB=false;
    private String Messaggio="";
    //private AdView adView;
    private TextView tv1;
    private TextView tv2;
    private int QualeColore=-1;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_onomastici);

        boolean Continua=true;

        context = this;
        VariabiliStaticheOnomastici.getInstance().setContext(this);
        VariabiliStaticheOnomastici.getInstance().setRubrica(getContentResolver());
        VariabiliStaticheOnomastici.getInstance().setAssets(this.getAssets());

        // new Banner(context, (RelativeLayout) findViewById(R.id.adMobFox2));
        // new Banner(context, (RelativeLayout) findViewById(R.id.adMobFox3));
        // new Banner(context, (RelativeLayout) findViewById(R.id.adMobFox4));
        // new Banner(context, (RelativeLayout) findViewById(R.id.adMobFox5));
        // new Banner(context, (RelativeLayout) findViewById(R.id.adMobFox6));

        tv1=(TextView) findViewById(R.id.txtColore1);
        tv2=(TextView) findViewById(R.id.txtColore2);

        DBLocaleOnomastici dbl=new DBLocaleOnomastici();
        dbl.CreaDB();

        VariabiliStaticheOnomastici.getInstance().setColorWidget(dbl.PrendeOpzioni(VariabiliStaticheOnomastici.getInstance().getContext()));

        SistemaSchermata();

        if (Continua) {
            TextView tChiamante=(TextView) findViewById(R.id.txtSanto);
            TextView tRoutine=(TextView) findViewById(R.id.txtGiorno);
            TextView tErrore=(TextView) findViewById(R.id.txtNumSettimana);
            GestioneDB varDB=new GestioneDB(context);
            if (Continua) {
                if (PulisceDB) {
                    Continua=varDB.EliminaDB(tRoutine, tErrore, "onCreate", tChiamante);
                } else {
                    Continua=varDB.ControllaDB(tRoutine, tErrore, "onCreate", tChiamante);
                    if (Continua) {
                        CaricaSantoDelGiorno();

                        // Gestione click su lista rilevati
                        /* final ListView lvR=(ListView) findViewById(R.id.lstNominativi);
                        lvR.setClickable(true);
                        lvR.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                                // String o = (String) lvR.getItemAtPosition(position);
                                String o = (String) nomiRilevati.get(position);

                                nomiSelezionati.add(o);
                                for (int i=0;i<nomiRilevati.size();i++) {
                                    if (nomiRilevati.get(i).equals(o)) {
                                        nomiRilevati.remove(i);
                                        break;
                                    }
                                }
                                AggiornaListe();
                            }
                        }); */

                        // Gestione click su lista selezionati
                        /* final ListView lvS=(ListView) findViewById(R.id.lstSelezionati);
                        lvS.setClickable(true);
                        lvS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                                // String o = (String) lvS.getItemAtPosition(position);
                                String o = (String) nomiSelezionati.get(position);

                                nomiRilevati.add(o);
                                for (int i=0;i<nomiSelezionati.size();i++) {
                                    if (nomiSelezionati.get(i).equals(o)) {
                                        nomiSelezionati.remove(i);
                                        break;
                                    }
                                }
                                AggiornaListe();
                            }
                        }); */

                        // Gestione click su lista messaggi
                        /* final ListView lvM=(ListView) findViewById(R.id.lstMessaggi);
                        lvM.setClickable(true);
                        lvM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                                // String o = (String) lvM.getItemAtPosition(position);
                                String o = (String) listaMessaggi.get(position);

                                TextView Santo = (TextView) findViewById(R.id.txtSanto);
                                String SSanto=(String) Santo.getText();
                                String NomeVis="";
                                if (SSanto.indexOf("/")>0){
                                    NomeVis=" onomastico ";
                                } else {
                                    NomeVis=" "+ SSanto +" ";
                                }
                                o=o.replace("%1", NomeVis);

                                Messaggio=o;
                                ImpostaMessaggio();
                            }
                        }); */

                        ImageView imgPulisceDB=(ImageView) findViewById(R.id.imgPulisceDB);
                        imgPulisceDB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GestioneDB db = new GestioneDB(context);
                                db.EliminaDB(tRoutine, tErrore, "DaTasto", tChiamante);
                            }
                        });

                        ImageView imgItaliano=(ImageView) findViewById(R.id.imgItaliano);
                        imgItaliano.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DBLocaleOnomastici dbl=new DBLocaleOnomastici();
                                dbl.SalvaLingua(VariabiliStaticheOnomastici.getInstance().getContext(), "ITALIANO");

                                ImpostaCampiTestoPerLingua();
                            }
                        });

                        ImageView imgInglese=(ImageView) findViewById(R.id.imgInglese);
                        imgInglese.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DBLocaleOnomastici dbl=new DBLocaleOnomastici();
                                dbl.SalvaLingua(VariabiliStaticheOnomastici.getInstance().getContext(), "INGLESE");

                                ImpostaCampiTestoPerLingua();
                            }
                        });

                        Button cmdRicercaSanto = (Button) findViewById(R.id.cmdRicNome);
                        cmdRicercaSanto.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                RicercaSanto rSanto=new RicercaSanto();
                                EditText rSanto1 = (EditText) findViewById(R.id.txtNomeRic);
                                if (rSanto1.getText().length()==0) {

                                } else {
                                    TextView tRitorno=(TextView) findViewById(R.id.txtSantiRilevati);
                                    int Quanti=rSanto.EffettuaRicercaSanto(context, tRitorno, rSanto1);
                                    if (Quanti==0) {
                                        if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
                                            tRitorno.setText("No Holy detected");
                                        } else {
                                            tRitorno.setText("Nessun Santo rilevato");
                                        }
                                    }
                                }
                            }
                        });

                        Button cmdRicercaSantoData = (Button) findViewById(R.id.cmdRicData);
                        cmdRicercaSantoData.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                RicercaSanto rSanto=new RicercaSanto();

                                DatePicker datePicker=(DatePicker) findViewById(R.id.datePicker1);
                                java.util.Date DataSel=getDateFromDatePicket(datePicker);
                                int Giorno=DataSel.getDate();
                                int Mese=DataSel.getMonth();
                                Mese++;

                                TextView tRitorno=(TextView) findViewById(R.id.txtSantiRilevatiData);
                                int Quanti=rSanto.RicercaSantoPerData(context, tRitorno, Giorno, Mese);
                                if (Quanti==0) {
                                    if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
                                        tRitorno.setText("No Holy detected");
                                    } else {
                                        tRitorno.setText("Nessun Santo rilevato");
                                    }
                                }
                            }
                        });

                        Button btnc1 = (Button) findViewById(R.id.cmdColoreSfondoWidget);
                        btnc1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QualeColore=1;

                                int color = PreferenceManager.getDefaultSharedPreferences(
                                        context).getInt(VariabiliStaticheOnomastici.getInstance().COLOR_PREFERENCE_KEY,
                                        Color.WHITE);
                                new ColorPickerDialog(context, MainOnomastici.this,
                                        color).show();
                            }
                        });

                        Button btnc2 = (Button) findViewById(R.id.cmdColoreTestoWidget);
                        btnc2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QualeColore=2;

                                int color = PreferenceManager.getDefaultSharedPreferences(
                                        context).getInt(VariabiliStaticheOnomastici.getInstance().COLOR_PREFERENCE_KEY,
                                        Color.WHITE);
                                new ColorPickerDialog(context, MainOnomastici.this,
                                        color).show();
                            }
                        });

                        final Intent intent=this.getIntent();
                        SeekBar sb=(SeekBar) findViewById(R.id.BarraColoreWidget);
                        sb.setMax(255);
                        sb.setProgress(VariabiliStaticheOnomastici.getInstance().getColorWidget().getTrasparenza());
                        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            public void onStopTrackingTouch(SeekBar bar) {
                                DBLocaleOnomastici dbl=new DBLocaleOnomastici();
                                dbl.ScriveOpzioni(VariabiliStaticheOnomastici.getInstance().getContext(), "BACKGROUND",
                                        VariabiliStaticheOnomastici.getInstance().getColorWidget());

                                WidgetOnomastici w=new WidgetOnomastici();
                                w.onReceive(VariabiliStaticheOnomastici.getInstance().getContext(), intent);
                            }

                            public void onStartTrackingTouch(SeekBar bar) {
                            }

                            public void onProgressChanged(SeekBar bar,int paramInt, boolean paramBoolean) {
                                VariabiliStaticheOnomastici.getInstance().getColorWidget().setTrasparenza(paramInt);
                            }
                        });

                        SeekBar sb2=(SeekBar) findViewById(R.id.BarraColoreWidgetTesto);
                        sb2.setMax(255);
                        sb2.setProgress(VariabiliStaticheOnomastici.getInstance().getColorWidget().getTrasparenzaT());
                        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            public void onStopTrackingTouch(SeekBar bar) {
                                DBLocaleOnomastici dbl=new DBLocaleOnomastici();
                                dbl.ScriveOpzioni(VariabiliStaticheOnomastici.getInstance().getContext(), "TESTO",
                                        VariabiliStaticheOnomastici.getInstance().getColorWidget());

                                WidgetOnomastici w=new WidgetOnomastici();
                                w.onReceive(VariabiliStaticheOnomastici.getInstance().getContext(), intent);
                            }

                            public void onStartTrackingTouch(SeekBar bar) {
                            }

                            public void onProgressChanged(SeekBar bar,int paramInt, boolean paramBoolean) {
                                VariabiliStaticheOnomastici.getInstance().getColorWidget().setTrasparenzaT(paramInt);
                            }
                        });

                        tv1.setTextColor(
                                Color.argb(VariabiliStaticheOnomastici.getInstance().getColorWidget().getTrasparenza(),
                                        VariabiliStaticheOnomastici.getInstance().getColorWidget().getRosso(),
                                        VariabiliStaticheOnomastici.getInstance().getColorWidget().getVerde(),
                                        VariabiliStaticheOnomastici.getInstance().getColorWidget().getBlu()));
                        tv2.setTextColor(
                                Color.argb(VariabiliStaticheOnomastici.getInstance().getColorWidget().getTrasparenzaT(),
                                        VariabiliStaticheOnomastici.getInstance().getColorWidget().getRossoT(),
                                        VariabiliStaticheOnomastici.getInstance().getColorWidget().getVerdeT(),
                                        VariabiliStaticheOnomastici.getInstance().getColorWidget().getBluT()));
                    }
                }
            }

            ImpostaCampiTestoPerLingua();
        }

        // StartTimerDiesgnaImmagineWidget();
        if (!VariabiliStaticheOnomastici.getInstance().isPartito()) {
            VariabiliStaticheOnomastici.getInstance().setPartito(true);
            this.finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                /* if (handlerTimer != null) {
                    handlerTimer.removeCallbacks(rTimer);
                    rTimer = null;
                    handlerTimer = null;
                } */
                this.finish();

                return false;
        }

        super.onKeyDown(keyCode, event);

        return false;
    }

    /* private Runnable runRiga;
    private int conta = 0;

  	private void StartTimerDiesgnaImmagineWidget() {
        final Handler hSelezionaRiga;

        hSelezionaRiga = new Handler();
        hSelezionaRiga.postDelayed(runRiga=new Runnable() {
            @Override
            public void run() {
                boolean ok = Widgets1.settaImmagineWidget();
                if (ok) {
                    hSelezionaRiga.removeCallbacks(runRiga);
                } else {
                	conta++;
                	if (conta==10) {
						hSelezionaRiga.removeCallbacks(runRiga);
					}
				}
            }
        }, 2000);

    } */

    private void CaricaSantoDelGiorno() {
        PrendeSantoClass Santo=new PrendeSantoClass();
        CampiRitornoSanti p = new CampiRitornoSanti();
        ImageView img=null;
        img=(ImageView) findViewById(R.id.imageView1);
        Santo.setImmagine(img);
        p=Santo.PrendeSanto(this, context,null);
        if (p.getNomeSanto().equals("ERRORE")) {
            // La routine di ricerca Santo ha trovato qualche impiccio
            TextView tSanto=(TextView) findViewById(R.id.txtSanto);
            tSanto.setText("");
            TextView tDescSanto=(TextView) findViewById(R.id.txtDescSanto);
            tDescSanto.setText("");
            TextView tGiorno=(TextView) findViewById(R.id.txtGiorno);
            tGiorno.setText("");
            TextView tDati=(TextView) findViewById(R.id.txtNumSettimana);
            tDati.setText("");
        } else {
            TextView tSanto=(TextView) findViewById(R.id.txtSanto);
            tSanto.setText(p.getNomeSanto());
            TextView tDescSanto=(TextView) findViewById(R.id.txtDescSanto);
            tDescSanto.setText(p.getDescrSanto());
            TextView tGiorno=(TextView) findViewById(R.id.txtGiorno);
            tGiorno.setText(p.getGiorno());
            TextView tDati=(TextView) findViewById(R.id.txtNumSettimana);
            tDati.setText(p.getDatiSettimana());
            ImageView imgView = (ImageView) findViewById(R.id.imageView1);
            imgView.setImageBitmap(BitmapFactory.decodeFile(p.getImmagine()));
        }

        LeggeNomiRubrica(p.getSinonimo());

        CaricaMessaggi();
    }

    private java.util.Date getDateFromDatePicket(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    /*
    private void InviaSMS() {
        int NumMessaggi=0;

        EditText rMess = (EditText) findViewById(R.id.txtMessaggio);
        String Messaggione = rMess.getText().toString();
        if (Messaggione.length() == 0) {
            Toast toast;
            if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
                toast=Toast.makeText(this,"Please, selected a message or add a new",Toast.LENGTH_LONG);
            } else {
                toast=Toast.makeText(this,"Selezionare un messaggio o inserirne uno nuovo",Toast.LENGTH_LONG);
            }
            toast.show();
        } else {
            if (nomiSelezionati.size()==0) {
                Toast toast;
                if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
                    toast=Toast.makeText(this,"Please, selected a addresse",Toast.LENGTH_LONG);
                } else {
                    toast=Toast.makeText(this,"Selezionare almeno un destinatario",Toast.LENGTH_LONG);
                }
                toast.show();
            } else {
                int i=0;
                int k=0;

                for (i=0; i<nomiSelezionati.size();i++) {
                    String Nome=nomiSelezionati.get(i);
                    for (k=0;k<nomiRilevatiNormale.size();k++) {
                        if (Nome.equals(nomiRilevatiNormale.get(i).toUpperCase().trim())) {
                            Nome=nomiRilevatiNormale.get(i);
                            break;
                        }
                    }
                    GestioneRubrica cnr=new GestioneRubrica();
                    if (!Nome.contains("@")) {
                        String tel=cnr.PrendeNumeroCellulareDaStringa(VariabiliStaticheOnomastici.getInstance().getRubrica(), Nome);
                        if (!tel.isEmpty()) {
                            PendingIntent pi = PendingIntent
                                    .getActivity(
                                            getApplicationContext(),
                                            0,
                                            new Intent(
                                                    getApplicationContext(),
                                                    MainOnomastici.class
                                            ),
                                            PendingIntent.FLAG_IMMUTABLE);
                            SmsManager sms = SmsManager.getDefault();
                            sms.sendTextMessage(tel, null, Messaggione, pi, null);
                            NumMessaggi++;
                        }
                    }
                }
                Toast toast;
                if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
                    toast=Toast.makeText(this, "Messages sends: "+NumMessaggi ,Toast.LENGTH_LONG);
                } else {
                    toast=Toast.makeText(this, "Messaggi inviati: "+NumMessaggi ,Toast.LENGTH_LONG);
                }
                toast.show();

				/* Button cmdInvia = (Button) findViewById(R.id.cmdInvia);
				cmdInvia.setEnabled(false); * /
            }
        }

    }

    private void ImpostaMessaggio() {
        if (Messaggio.length() == 0) {
            Toast toast;
            if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
                toast=Toast.makeText(this,"Please, selected a message or add a new",Toast.LENGTH_LONG);
            } else {
                toast=Toast.makeText(this,"Selezionare un messaggio o inserirne uno nuovo",Toast.LENGTH_LONG);
            }
            toast.show();
        } else {
            EditText Mess2=(EditText) findViewById(R.id.txtMessaggio);
            Mess2.setText(Messaggio);

            Toast toast;
            if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
                toast=Toast.makeText(this,"Messagge sended",Toast.LENGTH_LONG);
            } else {
                toast=Toast.makeText(this,"Messaggio impostato",Toast.LENGTH_LONG);
            }
            toast.show();
        }
    }
    */

    private void SistemaSchermata() {
        String[] Titoli;
        Titoli=new String[5];
        Titoli[1]="Home";
        Titoli[2]="Info";
        // Titoli[4]="Sms";
        Titoli[3]="Search";
        Titoli[4]="Setup";

        TabHost tabHost = (TabHost) findViewById(R.id.myTabHost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tabview1").setContent(R.id.tabview1).setIndicator(Titoli[1]));
        tabHost.addTab(tabHost.newTabSpec("tabview2").setContent(R.id.tabview2).setIndicator(Titoli[2]));
        tabHost.addTab(tabHost.newTabSpec("tabview3").setContent(R.id.tabview3).setIndicator(Titoli[3]));
        // tabHost.addTab(tabHost.newTabSpec("tabview4").setContent(R.id.tabview4).setIndicator(Titoli[4]));
        tabHost.addTab(tabHost.newTabSpec("tabview5").setContent(R.id.tabview5).setIndicator(Titoli[4]));

        TabWidget tw = (TabWidget)tabHost.findViewById(android.R.id.tabs);
        for (int i=0;i<4;i++) {
            View tabView = tw.getChildTabViewAt(i);
            TextView tv = (TextView)tabView.findViewById(android.R.id.title);
            tv.setText(Titoli[i+1]);
            tv.setTextSize(10);
        }
        int AltezzaTabHost=30;
        int AltezzaAdView=56;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int SchermoX=metrics.widthPixels;
        int SchermoY=metrics.heightPixels;

        int TreQuartiY=(SchermoY*3/4)-AltezzaTabHost;

        // Sezione Home
        int AltezzaHome=TreQuartiY;//-AltezzaAdView;
        LinearLayout lvH1=(LinearLayout) findViewById(R.id.ContHome2 );
        lvH1.setLayoutParams(new LinearLayout.LayoutParams(SchermoX , AltezzaHome));
        int UnTerzoDelloSchermoX=(SchermoX/3);
        int UnTerzoDelloSchermoY=(AltezzaHome/3);
        ImageView img=(ImageView) findViewById(R.id.imageView1);
        img.setLayoutParams(new LinearLayout.LayoutParams(UnTerzoDelloSchermoX , UnTerzoDelloSchermoY));
        int DiffeX=AltezzaHome-UnTerzoDelloSchermoY;

        // Sezione SMS
        int DiffeY=SchermoY-AltezzaTabHost;
        int MezzoSchermoX=(SchermoX/2)-20;

		/*TextView txtt1=(TextView) findViewById(R.id.txtTitolo1);
		TextView txtt2=(TextView) findViewById(R.id.txtTitolo2);
		txtt1.setLayoutParams(new LinearLayout.LayoutParams(MezzoSchermoX , 35));
		txtt2.setLayoutParams(new LinearLayout.LayoutParams(MezzoSchermoX , 35));*/

        // DiffeY-=35;
        /* int DimensioneListeNomi=(int) (DiffeY);
        ListView lstN=(ListView) findViewById(R.id.lstNominativi);
        ListView lstS=(ListView) findViewById(R.id.lstSelezionati);
        lstN.setLayoutParams(new LinearLayout.LayoutParams(MezzoSchermoX , DimensioneListeNomi));
        lstS.setLayoutParams(new LinearLayout.LayoutParams(MezzoSchermoX , DimensioneListeNomi));
        DiffeY-=DimensioneListeNomi; */

		/*TextView txtt3=(TextView) findViewById(R.id.txtTitolo21);
		txtt3.setLayoutParams(new LinearLayout.LayoutParams(SchermoX , 35));
		DiffeY-=35;*/

        /* ListView lstM=(ListView) findViewById(R.id.lstMessaggi);
        lstM.setLayoutParams(new LinearLayout.LayoutParams(SchermoX-50 , DimensioneListeNomi));
        DiffeY-=DimensioneListeNomi; */

        // LinearLayout lvP1=(LinearLayout) findViewById(R.id.LayPagina1 );
        // lvP1.setLayoutParams(new LinearLayout.LayoutParams(SchermoX-45 , SchermoY));

        // Sezione ricerca
        // LinearLayout lvP3=(LinearLayout) findViewById(R.id.LayPagina3 );
        // lvP3.setLayoutParams(new LinearLayout.LayoutParams(SchermoX-45 , SchermoY));
        // LinearLayout lvP4=(LinearLayout) findViewById(R.id.LayPagina4 );
        // lvP4.setLayoutParams(new LinearLayout.LayoutParams(SchermoX-45 , SchermoY));

        // Sezione Utility
        //LinearLayout lvP2=(LinearLayout) findViewById(R.id.LayPagina2 );
        //lvP2.setLayoutParams(new LinearLayout.LayoutParams(SchermoX-35, SchermoY));
    }

    private void RiempieLista(ListView Lista, String Nomi[]) {
        ArrayList<Person> personListR=new ArrayList<Person>();
        Person [] people={};
        String Nomello="";
        GestioneRubrica GR=new GestioneRubrica();

        people=new Person[Nomi.length];
        int i=0;
        for (i=0;i<Nomi.length;i++) {
            Nomello=Nomi[i];
            if (Nomello.contains("@")) {
                people[i]= new Person(Nomi[i], R.drawable.mail);
            } else {
                people[i]= new Person(Nomi[i], R.drawable.utente);
            }
            personListR.add(people[i]);
        }

        ArrayList<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();
        for(i=0;i<personListR.size();i++){
            Person p=personListR.get(i);
            HashMap<String,Object> personMap=new HashMap<String, Object>();
            Nomello=p.getName();
            personMap.put("image", p.getPhotoRes());
            personMap.put("name", Nomello);
            data.add(personMap);
        }
        String[] from={"image","name"};
        int[] to={R.id.personImage,R.id.personName};
        SimpleAdapter adapter=new SimpleAdapter(
                getApplicationContext(),
                data,
                R.layout.listview_nomi,
                from,
                to);
        Lista.setAdapter(adapter);
    }

    private void AggiornaListe(){
        /* String NomiR[];
        NomiR=new String[nomiRilevati.size()];

        int i=0;
        for (i=0;i<nomiRilevati.size();i++) {
            NomiR[i]=nomiRilevati.get(i);
        }
        ListView listaR=(ListView) findViewById(R.id.lstNominativi);
        RiempieLista(listaR, NomiR);

        String NomiS[];
        NomiS=new String[nomiSelezionati.size()];

        for (i=0;i<nomiSelezionati.size();i++) {
            NomiS[i]=nomiSelezionati.get(i);
        }
        ListView listaS=(ListView) findViewById(R.id.lstSelezionati);
        RiempieLista(listaS, NomiS); */
    }

    private void CaricaMessaggi() {
        /* GestioneDB varDB=new GestioneDB(context);

        TextView tErrore=(TextView) findViewById(R.id.txtSanto);
        TextView tRoutine=(TextView) findViewById(R.id.txtGiorno);
        TextView tChiamante=(TextView) findViewById(R.id.txtNumSettimana);
        SQLiteDatabase myDB= varDB.ApreDB(tRoutine, tErrore,"CaricaMessaggi",tChiamante);

        String Sql="";
        String Mess="";
        listaMessaggi=new ArrayList<String>();

        try {
            if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
                Sql="SELECT MessInglese FROM Messaggi Order By NumMess;";
            } else {
                Sql="SELECT Messaggio FROM Messaggi Order By NumMess;";
            }
            Cursor c = myDB.rawQuery(Sql , null);
            c.moveToFirst();
            do {
                Mess=c.getString(0);
                listaMessaggi.add(Mess);
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            int a;
            a=0;
        }
        varDB.ChiudeDB(myDB,tRoutine, tErrore, "CaricaMessaggi", tChiamante);


        String MessaggiS[];
        MessaggiS=new String[listaMessaggi.size()];
        int i=0;

        for (i=0;i<listaMessaggi.size();i++) {
            MessaggiS[i]=listaMessaggi.get(i);
        }
        ListView listaM=(ListView) findViewById(R.id.lstMessaggi);
        RiempieListaMessaggi(listaM, MessaggiS); */
    }

    private void RiempieListaMessaggi(ListView Lista, String Mess[]) {
        /* ArrayList<Messaggi> personListR=new ArrayList<Messaggi>();
        Messaggi [] people={};

        people=new Messaggi[Mess.length];
        int i=0;
        for (i=0;i<Mess.length;i++) {
            people[i]= new Messaggi(Mess[i]);
            personListR.add(people[i]);
        }

        ArrayList<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();
        for(i=0;i<personListR.size();i++){
            Messaggi p=personListR.get(i);
            HashMap<String,Object> personMap=new HashMap<String, Object>();
            personMap.put("name", p.getMessaggio());
            data.add(personMap);
        }
        String[] from={"name"};
        int[] to={R.id.messaggi};
        SimpleAdapter adapter=new SimpleAdapter(
                getApplicationContext(),
                data,
                R.layout.listview_messaggi,
                from,
                to);
        Lista.setAdapter(adapter); */
    }

    private void LeggeNomiRubrica(String Sinonimi) {
        List<String> Santi=new ArrayList<String>();
        int Quale=0;
        String id="";
        String name ="";
        String nameNormale ="";
        TextView tSanto=(TextView) findViewById(R.id.txtSanto);
        String Santo=(String) tSanto.getText();
        int i=0;

        if (Santo.contains("/")) {
            String Santo2="";
            while (Santo.contains("/")) {
                Santo2=Santo.substring(0,Santo.indexOf("/")-1).trim().toUpperCase().replace("'","''");
                Santi.add(Santo2);
                Santo=Santo.substring(Santo.indexOf("/")+1,Santo.length());
            }
            Santi.add(Santo.trim().toUpperCase());
        } else {
            Santi.add(Santo.trim().toUpperCase());
        }

        if (Sinonimi != null && Sinonimi.contains("/")) {
            String Santo2="";
            while (Sinonimi.contains("/")) {
                Santo2=Sinonimi.substring(0,Sinonimi.indexOf("/")-1).trim().toUpperCase().replace("'","''");
                Santi.add(Santo2);
                Sinonimi=Sinonimi.substring(Sinonimi.indexOf("/")+1,Sinonimi.length());
            }
            Santi.add(Sinonimi.trim().toUpperCase());
        } else {
            if (Sinonimi != null && Sinonimi.length()>0) {
                Santi.add(Sinonimi.trim().toUpperCase());
            }
        }

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {

                try {
                    id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).toUpperCase().trim();
                    nameNormale= cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                } catch (Exception e) {
                    id="";
                    name="";
                    nameNormale="";
                }

                // myDB.execSQL("Insert Into Nomi Values ('"+name.replace("'","''")+"');");

                for (i=1;i<=Santi.size();i++) {
                    if (name.contains(Santi.get(i-1))){
                        nomiRilevati.add(name);
                        nomiRilevatiNormale.add(nameNormale);
                        break;
                    }
                }
            }
        }
        Quale=nomiRilevati.size();
        cur.close();

        // varDB.ChiudeDB(myDB);

        boolean Visibile=true;

        TextView dRicorrenze = (TextView) findViewById(R.id.txtRicorrenze);
        if (Quale>0) {
			/* ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomiRilevati);
	        ListView listaNomi=(ListView) findViewById(R.id.lstNominativi);
			listaNomi.setAdapter(adapter); */

            if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
                dRicorrenze.setText("Recurrences detected: " + Quale);
            } else {
                dRicorrenze.setText("Ricorrenze rilevate: " + Quale);
            }
        } else {
            dRicorrenze.setText("");
            Visibile=false;
        }

        AggiornaListe();

		/* Button cmdInvia = (Button) findViewById(R.id.cmdInvia);
		cmdInvia.setEnabled(Visibile); */
    }

    private class Person {
        private String name;
        private int photoRes;
        public Person(String name, int photoRes) {
            super();
            this.name = name;
            this.photoRes = photoRes;
        }
        public String getName() {
            return name;
        }

        public int getPhotoRes() {
            return photoRes;
        }
    }

    private class Messaggi {
        private String Messaggio;
        private int photoRes;
        public Messaggi(String Messaggio) {
            super();
            this.Messaggio = Messaggio;
        }
        public String getMessaggio() {
            return Messaggio;
        }
    }

    @Override
    public void colorChanged(int color) {
        DBLocaleOnomastici dbl=new DBLocaleOnomastici();

        int Trasparenza;

        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(
                VariabiliStaticheOnomastici.getInstance().COLOR_PREFERENCE_KEY, color).commit();
        if (QualeColore==1) {
            tv1.setTextColor(color);

            Trasparenza= VariabiliStaticheOnomastici.getInstance().getColorWidget().getTrasparenza();
            VariabiliStaticheOnomastici.getInstance().getColorWidget().setTrasparenza(Trasparenza);
            VariabiliStaticheOnomastici.getInstance().getColorWidget().setRosso(Color.red(color));
            VariabiliStaticheOnomastici.getInstance().getColorWidget().setVerde(Color.green(color));
            VariabiliStaticheOnomastici.getInstance().getColorWidget().setBlu(Color.blue(color));

            dbl.ScriveOpzioni(VariabiliStaticheOnomastici.getInstance().getContext(), "BACKGROUND", VariabiliStaticheOnomastici.getInstance().getColorWidget());
        }
        if (QualeColore==2) {
            tv2.setTextColor(color);

            Trasparenza= VariabiliStaticheOnomastici.getInstance().getColorWidget().getTrasparenzaT();
            VariabiliStaticheOnomastici.getInstance().getColorWidget().setTrasparenzaT(Trasparenza);
            VariabiliStaticheOnomastici.getInstance().getColorWidget().setRossoT(Color.red(color));
            VariabiliStaticheOnomastici.getInstance().getColorWidget().setVerdeT(Color.green(color));
            VariabiliStaticheOnomastici.getInstance().getColorWidget().setBluT(Color.blue(color));

            dbl.ScriveOpzioni(VariabiliStaticheOnomastici.getInstance().getContext(), "TESTO", VariabiliStaticheOnomastici.getInstance().getColorWidget());
        }

        WidgetOnomastici w=new WidgetOnomastici();
        Intent intent=this.getIntent();
        w.onReceive(VariabiliStaticheOnomastici.getInstance().getContext(), intent);
    }

    private void ImpostaCampiTestoPerLingua() {
        String tR1;
        String tR2;
        String cR1;
        String cR2;
        String tTS;
        String tCS;
        String tTT;
        String tCT;
        String tC1;
        String tC2;
        String tT1;
        String tT2;
        String tT21;
        String tT212;
        String cI;

        if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
            tR1="Saint search";
            cR1="Search";
            tR2="Saint search by date";
            cR2="Search";
            tTS="Widget transparency";
            tCS="Widget background color";
            tTT="Text transparency";
            tCT="Widget text color";
            tC1="Example";
            tC2="Example";
            /* tT1="Detected";
            tT2="Selected";
            tT21="Messages list";
            tT212="Text"; */
            cI="Send";
        } else {
            tR1="Ricerca Santo";
            cR1="Ricerca";
            tR2="Ricerca per data";
            cR2="Ricerca";
            tTS="Trasparenza widget";
            tCS="Colore sfondo widget";
            tTT="Trasparenza testo";
            tCT="Colore testo widget";
            tC1="Esempio";
            tC2="Esempio";
            /* tT1="Rilevati";
            tT2="Selezionati";
            tT21="Lista messaggi";
            tT212="Testo"; */
            cI="Invia";
        }

        Button cr1=(Button) findViewById(R.id.cmdRicNome);
        cr1.setText(cR1);

        Button cr2=(Button) findViewById(R.id.cmdRicData);
        cr2.setText(cR2);

        TextView tr1=(TextView) findViewById(R.id.txtTitoloRic1);
        tr1.setText(tR1);

        TextView tr2=(TextView) findViewById(R.id.txtTitoloRicData);
        tr2.setText(tR2);

        TextView tts=(TextView) findViewById(R.id.txtTitoloTraspSfondo);
        tts.setText(tTS);

        TextView tcs=(TextView) findViewById(R.id.txtColoreSfondoWidget);
        tcs.setText(tCS);

        TextView ttt=(TextView) findViewById(R.id.txtTitoloTraspTesto);
        ttt.setText(tTT);

        TextView tct=(TextView) findViewById(R.id.txtColoreTestoWidget);
        tct.setText(tCT);

        TextView tc1=(TextView) findViewById(R.id.txtColore1);
        tc1.setText(tC1);

        TextView tc2=(TextView) findViewById(R.id.txtColore2);
        tc2.setText(tC2);

        /* TextView tt1=(TextView) findViewById(R.id.txtTitolo1);
        tt1.setText(tT1);

        TextView tt2=(TextView) findViewById(R.id.txtTitolo2);
        tt2.setText(tT2);

        TextView tt21=(TextView) findViewById(R.id.txtTitolo21);
        tt21.setText(tT21);

        TextView tt212=(TextView) findViewById(R.id.txtTitolo212);
        tt212.setText(tT212); */

        CaricaSantoDelGiorno();

        WidgetOnomastici w=new WidgetOnomastici();
        Intent intent=this.getIntent();
        w.onReceive(VariabiliStaticheOnomastici.getInstance().getContext(), intent);
    }
}
