package com.looigi.wallpaperchanger2.classiPlayer.WebServices;

import android.content.Context;

import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiPlayer.DownloadBrano;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaPreferiti;
import com.looigi.wallpaperchanger2.classiPlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classiPlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classiPlayer.db_dati_player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChiamateWsPlayer implements TaskDelegatePlayer {
    private static final String NomeMaschera = "CHIAMATEWSPLAYER";
    private LetturaWSAsincronaPlayer bckAsyncTask;

    private final String RadiceWS = VariabiliStatichePlayer.UrlWS + "/";
    private final String ws = "wsMobile.asmx/";
    private final String ws2 = "wsLWP.asmx/";
    private final String NS="http://wsMobile2.org/";
    private final String SA="http://wsMobile2.org/";
    private final String NS2="http://csaricanuovai.org/";
    private final String SA2="http://csaricanuovai.org/";
    private String TipoOperazione = "";
    private Context context;

    public ChiamateWsPlayer(Context context) {
        this.context = context;
    }

    public void RitornaImmaginiArtista(String Artista) {
        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ritorna Immagini Artista");

        String Urletto="RitornaImmaginiArtista?Artista=" + Artista;

        TipoOperazione = "RitornaImmaginiArtista";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                10000,
                true,
                true,
                false,
                -1);
    }

    public void RicercaBrani(String Ricerca) {
        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ricerca Brani");

        String Urletto="RitornaBrani?Ricerca=" + Ricerca;

        TipoOperazione = "RitornaBrani";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                true,
                true,
                false,
                -1);
    }

    public void RicaricaBrani() {
        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ricarica brani. Eliminazione JSON");

        String Urletto="EliminaJSON?idUtente=" + VariabiliStatichePlayer.getInstance().getUtente().getId();

        TipoOperazione = "EliminaJSON";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                10000,
                true,
                true,
                false,
                -1);
    }

    public void RicaricaBrani2() {
        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ricarica brani2. Creazione JSON");

        String Urletto="RefreshCanzoniHard?idUtente=" + VariabiliStatichePlayer.getInstance().getUtente().getId();

        TipoOperazione = "RefreshCanzoniHard";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                200000,
                true,
                true,
                false,
                -1);
    }

    public void RitornaStelleBrano() {
        if (VariabiliStatichePlayer.getInstance().getUltimoBrano() == null) {
            return;
        }
        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ritorna Stelle Brano " + VariabiliStatichePlayer.getInstance().getUltimoBrano().getIdBrano());

        String Urletto="RitornaStelleBrano?idUtente=" + VariabiliStatichePlayer.getInstance().getUtente().getId() + "&idBrano=" + VariabiliStatichePlayer.getInstance().getUltimoBrano().getIdBrano();

        TipoOperazione = "RitornaStelleBrano";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                false,
                true,
                false,
                -1);
    }

    public void RitornaListaAlbum(String Artista, String Ricerca) {
        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ritorna lista Album");

        Artista = UtilityPlayer.getInstance().ConverteNome(Artista);

        String Urletto="RitornaListaAlbumArtista?Artista=" + Artista + "&Ricerca=" + Ricerca;

        TipoOperazione = "RitornaListaAlbumArtista";
        // ControllaTempoEsecuzione = true;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                false,
                true,
                false,
                -1);
    }

    public void RitornaListaBrani(String Artista, String Album, String Ricerca) {
        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ritorna lista brani per Artista " + Artista + " e album " + Album);

        Artista = UtilityPlayer.getInstance().ConverteNome(Artista);
        Album = UtilityPlayer.getInstance().ConverteNome(Album);

        String Urletto="RitornaListaBraniAlbumArtista?Artista=" + Artista + "&Album=" + Album + "&Ricerca=" + Ricerca;

        TipoOperazione = "RitornaListaBraniAlbumArtista";
        // ControllaTempoEsecuzione = true;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                false,
                true,
                false,
                -1);
    }

    public void RitornaListaTags() {
        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ritorna lista Tags");

        String Urletto="RitornaListaTags";

        TipoOperazione = "RitornaListaTags";
        // ControllaTempoEsecuzione = true;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                false,
                true,
                false,
                -1);
    }

    public void RitornaListaArtisti(boolean ApreDialog) {
        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ritorna lista Artisti");

        String Urletto="RitornaArtisti";

        TipoOperazione = "RitornaArtisti";
        // ControllaTempoEsecuzione = true;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                50000,
                ApreDialog,
                true,
                false,
                -1);
    }

    private long lastCall = -1;

    public void RitornaBranoDaID(String idBrano) {
        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Caricamento prossimo brano.");

        long ora = new Date().getTime();
        if (ora - lastCall < 2000) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Caricamento prossimo brano. Esco per troppo veloce: " + (ora - lastCall));

            return;
        }
        lastCall = ora;

        String Stelle = Integer.toString(VariabiliStatichePlayer.getInstance().getStelleDaRicercare());
        String StelleSuperiori = VariabiliStatichePlayer.getInstance().isStelleSuperiori() ? "S" : "N";
        String MaiAscoltata = VariabiliStatichePlayer.getInstance().isRicercaMaiAscoltata() ? "S" : "N";
        if (!VariabiliStatichePlayer.getInstance().isRicercaStelle()) {
            Stelle = "";
            StelleSuperiori = "";
        } else {
            if (MaiAscoltata.equals("S")) {
                Stelle = "0";
                StelleSuperiori = "";
            }
        }

        String Testo = VariabiliStatichePlayer.getInstance().getTestoDaRicercare();
        String TestoNon = VariabiliStatichePlayer.getInstance().getTestoDaNonRicercare();
        if (!VariabiliStatichePlayer.getInstance().isRicercaTesto()) {
            Testo = "";
            TestoNon = "";
        }

        String Preferiti = VariabiliStatichePlayer.getInstance().getPreferiti();
        String PreferitiElimina = VariabiliStatichePlayer.getInstance().getPreferitiElimina();
        String AndOrPref = VariabiliStatichePlayer.getInstance().isAndOrPref() ? "S" : "N";
        if (!VariabiliStatichePlayer.getInstance().isRicercaPreferiti()) {
            Preferiti = "";
            PreferitiElimina = "";
            AndOrPref = "";
        }

        String Tags = VariabiliStatichePlayer.getInstance().getPreferitiTags();
        String TagsElimina = VariabiliStatichePlayer.getInstance().getPreferitiEliminaTags();
        String AndOrTags = VariabiliStatichePlayer.getInstance().isAndOrTags() ? "S" : "N";
        if (!VariabiliStatichePlayer.getInstance().isRicercaTags()) {
            Tags = "";
            TagsElimina = "";
            AndOrTags = "";
        }

        String DataSuperiore = "";
        String DataInferiore = "";

        if (VariabiliStatichePlayer.getInstance().isDate()) {
            if (VariabiliStatichePlayer.getInstance().isDataSuperiore()) {
                if (!VariabiliStatichePlayer.getInstance().getTxtDataSuperiore().isEmpty()) {
                    DataSuperiore = VariabiliStatichePlayer.getInstance().getTxtDataSuperiore();
                }
            }

            if (VariabiliStatichePlayer.getInstance().isDataInferiore()) {
                if (!VariabiliStatichePlayer.getInstance().getTxtDataInferiore().isEmpty()) {
                    DataInferiore = VariabiliStatichePlayer.getInstance().getTxtDataInferiore();
                }
            }
        }

        int idUltimoBrano;
        if (VariabiliStatichePlayer.getInstance().getUltimoBrano() != null) {
            idUltimoBrano = VariabiliStatichePlayer.getInstance().getUltimoBrano().getIdBrano();
        } else {
            idUltimoBrano = 1;
        }
        String Urletto="RitornaProssimoBranoMobile?";
        Urletto += "idUtente=" + VariabiliStatichePlayer.getInstance().getUtente().getId();
        Urletto += "&Random=S";
        Urletto += "&idBranoAttuale=" + idUltimoBrano;
        Urletto += "&Stelle=" + Stelle;
        Urletto += "&RicercaTesto=" + Testo;
        Urletto += "&Tags=" + Tags;
        Urletto += "&Preferiti=" + Preferiti;
        Urletto += "&TagsElimina=" + TagsElimina;
        Urletto += "&PreferitiElimina=" + PreferitiElimina;
        Urletto += "&BranoEsatto=" + idBrano;
        Urletto += "&DataSuperiore=" + DataSuperiore;
        Urletto += "&DataInferiore=" + DataInferiore;
        Urletto += "&StelleSuperiori=" + StelleSuperiori;
        Urletto += "&AndOrTags=" + AndOrTags;
        Urletto += "&AndOrPref=" + AndOrPref;
        Urletto += "&RicercaNonTesto=" + TestoNon;

        // ControllaTempoEsecuzione = true;
        TipoOperazione = "RitornaProssimoBranoMobile";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                25000,
                false,
                true,
                false,
                -1);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog, boolean ChiamataDiretta,
                       boolean ControllaTempoEsecuzione, int Stelle) {

        UtilityPlayer.getInstance().Attesa(true);
        UtilityPlayer.getInstance().AggiornaOperazioneInCorso(tOperazione);

        bckAsyncTask = new LetturaWSAsincronaPlayer(
                context,
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                "0", // TimeStampAttuale,
                this);
        bckAsyncTask.execute(Urletto);
    }

    @Override
    public void TaskCompletionResult(String result) {
        UtilityPlayer.getInstance().Attesa(false);
       /* boolean Ok = true;

        if (ControllaTempoEsecuzione) {
            if (differenza > 30) {
                // Ci ha messo troppo tempo
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". Troppo tempo a rispondere: " + differenza);

                UtilityPlayer.getInstance().ImpostaStatoReteOFF();
                accesoStatoReteMancante = true;
            } else {
                if (accesoStatoReteMancante) {
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". Ripristino stato rete");
                    accesoStatoReteMancante = false;
                    UtilityPlayer.getInstance().ImpostaStatoReteON();
                }
            }
        } */

        boolean Scoda = true;

        // if (Ok) {
            switch (TipoOperazione) {
                case "RitornaStelleBrano":
                    fRitornaStelleBrano(result);
                    break;
                case "RitornaArtisti":
                    RitornaArtisti(result);
                    break;
                case "RitornaProssimoBranoMobile":
                    CaricaBrano(result);
                    break;
                case "ScaricaTesto":
                    fAggiornaTesto(result);
                    break;
                case "RitornaBrani":
                    fRitornaBrani(result);
                    break;
            }
    }

    private void fAggiornaTesto(String result) {
        boolean ritorno = ControllaRitorno("Aggiorna testo", result);
        if (!ritorno) {
            // Utility.getInstance().VisualizzaMessaggio(result);
        } else {
            String[] t = result.split("\\|", -1);
            String testo = t[0];
            testo = testo.replace("§", "\n");
            testo = testo.replace("%20", " ");
            String testo2 = testo.substring(0, 30);

            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Aggiornamento testo brano eseguito: " + testo2);

            // Files.getInstance().ScriveFile(VariabiliGlobali.getInstance().getPathTesto(), VariabiliGlobali.getInstance().getNomeFileTesto(), testo);

            VariabiliStatichePlayer.getInstance().getUltimoBrano().setTesto(testo);

            // db_dati db = new db_dati();
            // db.aggiornaTestoBrano(VariabiliGlobali.getInstance().getIdTesto(), testo);
        }
    }

    private void fRitornaBrani(String result) {
        boolean ritorno = ControllaRitorno("Ritorna brani", result);
        if (!ritorno) {
            // UtilityPlayer.getInstance().VisualizzaMessaggio(result);
        } else {
            String[] lista = result.split("§", -1);
            for (String l : lista) {
                if (!l.isEmpty()) {
                    String[] Campi = l.split(";", -1);

                    try {
                        StrutturaBrano s = new StrutturaBrano();
                        s.setIdBrano(Integer.parseInt(Campi[0]));
                        s.setAnno(Campi[1]);
                        s.setArtista(Campi[2]);
                        s.setAlbum(Campi[3]);
                        s.setBrano(Campi[4]);
                        s.setTraccia(Campi[5]);
                        if (Campi[6].isEmpty()) {
                            s.setAscoltata(0);
                        } else {
                            s.setAscoltata(Integer.parseInt(Campi[6]));
                        }
                        if (Campi[7].isEmpty()) {
                            s.setBellezza(0);
                        } else {
                            s.setBellezza(Integer.parseInt(Campi[7]));
                        }
                        s.setData(Campi[8]);
                        if (Campi[9].isEmpty()) {
                            s.setDimensione(0L);
                        } else {
                            s.setDimensione(Long.parseLong(Campi[9]));
                        }
                        s.setEstensione(Campi[10]);
                        try {
                            s.setTags(Campi[11]);
                        } catch (Exception ignored) {
                            s.setTags("");
                        }
                        s.setImmagini(new ArrayList<>());
                        s.setTipoBrano(0);

                        // VariabiliStatichePlayer.getInstance().getListaBraniRicercati().add(s);
                    } catch (Exception e) {
                        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Errore ritorno RitornaBrani: " +
                                UtilityDetector.getInstance().PrendeErroreDaException(e));
                    }
                }
            }

        }
        // UtilityPlayer.getInstance().RiempieListaRicerche();
    }

    private void fRitornaStelleBrano(String result) {
        int Stelle;

        boolean ritorno = ControllaRitorno("Ritorna stelle brano", result);
        if (!ritorno) {
            Stelle = 0;
        } else {
            Stelle = Integer.parseInt(result);
        }

        VariabiliStatichePlayer.getInstance().getUltimoBrano().setBellezza(Stelle);
    }

    private void RitornaArtisti(String result) {
        boolean ritorno = ControllaRitorno("Ritorna Artisti", result);
        if (!ritorno) {
            // UtilityPlayer.getInstance().VisualizzaMessaggio(result);
        } else {
            // Files.getInstance().EliminaFile(VariabiliStatichePlayer.getInstance().getPercorsoDIR() + "/Liste", "ListaArtisti.txt");
            // Files.getInstance().ScriveFile(VariabiliStatichePlayer.getInstance().getPercorsoDIR() + "/Liste", "ListaArtisti.txt", result);

            RitornaArtisti2(result);
        }
    }

    public void RitornaArtisti2(String result) {
        boolean ritorno = ControllaRitorno("Ritorna Artisti 2", result);
        if (!ritorno) {
            // UtilityPlayer.getInstance().VisualizzaMessaggio(result);
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ritorno artisti");

            String[] Globale = result.split("§", -1);
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ritorno artisti -> " + Globale.length);

            for (int i = 0; i < Globale.length; i++) {
                if (!Globale[i].trim().replace("\n", "").isEmpty()) {
                    try {
                        String[] dati = Globale[i].split("\\|", -1);
                        String Artista = dati[0];
                        String Immagine = dati[1];
                        // String UrlImmagine = VariabiliStatichePlayer.getInstance().getPercorsoBranoMP3SuSD() +
                        //         "/ImmaginiMusica" + Immagine;
                        List<String> listaTags = new ArrayList<>();
                        if (!dati[2].isEmpty()) {
                            String[] Tags = dati[2].split("%", -1);
                            for (int k = 0; k < Tags.length; k++) {
                                if (!Tags[k].isEmpty()) {
                                    listaTags.add(Tags[k]);
                                }
                            }
                        }

                        StrutturaPreferiti sa = new StrutturaPreferiti();
                        sa.setNomeArtista(Artista);
                        sa.setTags(listaTags);
                        sa.setImmagine(Immagine);

                        // VariabiliStatichePlayer.getInstance().AggiungeArtista(sa);
                    } catch (Exception ignored) {
                        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ritorno artisti. Errore su parse (" + Globale[i] + "): " +
                                UtilityDetector.getInstance().PrendeErroreDaException(ignored));
                    }
                }
            }
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Ritorno artisti effettuato");
        }
    }

    private boolean ControllaRitorno(String Operazione, String result) {
        if (result.contains("ERROR:")) {
            if (result.contains("JAVA.NET.UNKNOWNHOSTEXCEPTION") || result.contains("SOCKETTIMEOUTEXCEPTION")) {
                // UtilityPlayer.getInstance().ImpostaStatoReteOFF();
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, Operazione + ": Rete non presente o timeout nella chiamata.");
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, result);
            }

            return false;
        } else {
            return true;
        }
    }

    public void CaricaBrano(String result) {
        boolean ritorno = ControllaRitorno("Carica Brano", result);
        if (!ritorno) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Carica brano: Esco per result non valido");

            return;
        }

        if (result.contains("|")) {
            String[] Globale = result.split("\\|", -1);
            String[] DatiBrano = Globale[0].split(";", -1);
            String[] Immagini = Globale[1].split("§", -1);
            String[] TestoEAltro = Globale[2].split(";", -1);
            if (Globale[2].equals(";;;;")) {
                TestoEAltro = new String[]{"", "", "", ""};
            }

            VariabiliStatichePlayer.getInstance().setBraniTotali(Integer.parseInt(DatiBrano[1]));

            StrutturaBrano s = new StrutturaBrano();
            s.setIdBrano(Integer.parseInt(DatiBrano[0]));

            s.setQuantiBrani(Integer.parseInt(DatiBrano[1]));
            s.setArtista(DatiBrano[3]);

            s.setAlbum(DatiBrano[4]);

            s.setBrano(DatiBrano[5]);
            String Anno = DatiBrano[6];
            for (int i = Anno.length() + 1; i < 5; i++) {
                Anno = "0" + Anno;
            }
            s.setAnno(Anno);
            String Traccia = DatiBrano[7];
            if (Traccia.length() == 1) {
                Traccia = "0" + Traccia;
            }
            s.setTraccia(Traccia);
            s.setEstensione(DatiBrano[8]);
            s.setData(DatiBrano[9]);
            try {
                if (!DatiBrano[10].isEmpty()) {
                    s.setDimensione(Long.parseLong(DatiBrano[10]) / 1024L);
                } else {
                    s.setDimensione(0L);
                }
            } catch (Exception ignored) {
                s.setDimensione(0L);
            }
            try {
                String t = DatiBrano[11].replace("*PV*", ";");
                String[] tt = t.split(";", -1);
                String tags = "";
                for (int i = 0; i < tt.length; i++) {
                    if (!tt[i].isEmpty()) {
                        tags += tt[i] + ";";
                    }
                }
                if (tags.length() > 0) {
                    tags = tags.substring(0, tags.length() - 1);
                }
                s.setTags(tags);
            } catch (Exception ignored) {
                s.setTags("");
            }

            String UrlBrano = VariabiliStatichePlayer.PercorsoBranoMP3SuURL +
                    "/MP3/" + DatiBrano[3] + "/" + Anno + "-" + DatiBrano[4] + "/" +
                    Traccia + "-" + DatiBrano[5] + DatiBrano[8];
            s.setUrlBrano(UrlBrano);

            String PathBrano = context.getFilesDir() + "/Player/" +
                    "Brani/" + DatiBrano[3] + "/" + Anno + "-" + DatiBrano[4] + "/" +
                    Traccia + "-" + DatiBrano[5] + DatiBrano[8];
            s.setPathBrano(PathBrano);

            String CartellaBrano = context.getFilesDir() + "/Player/" +
                    "Brani/" + DatiBrano[3] + "/" + Anno + "-" + DatiBrano[4];
            s.setCartellaBrano(CartellaBrano);

            if (UtilityDetector.getInstance().EsisteFile(PathBrano)) {
                s.setEsisteBranoSuDisco(true);
            } else {
                s.setEsisteBranoSuDisco(false);
            }

            List<StrutturaImmagini> ListaImmagini;
            ListaImmagini = new ArrayList<>();
            String ImmagineDaImpostare = "";
            StrutturaImmagini StruttImmDaImpostare = new StrutturaImmagini();
            for (int i = 0; i < Immagini.length; i++) {
                if (!Immagini[i].isEmpty()) {
                    String[] Imm2 = Immagini[i].split(";", -1);
                    StrutturaImmagini Imm = new StrutturaImmagini();
                /* if (Imm2[2].toUpperCase().contains("COVER_")) {
                    Imm.setAlbum(Imm2[1]);
                } else {
                } */
                    Imm.setAlbum(Imm2[1]);
                    Imm.setNomeImmagine(Imm2[2]);

                    String UrlImmagine = VariabiliStatichePlayer.PercorsoBranoMP3SuURL +
                            "/ImmaginiMusica/" + DatiBrano[3] + "/" + Imm2[1] + "/" +
                            Imm2[2];
                    Imm.setUrlImmagine(UrlImmagine);

                    String PathImmagine = context.getFilesDir() + "/Player/" +
                            "/ImmaginiMusica/" + DatiBrano[3] + "/" + Imm2[1] + "/" +
                            Imm2[2];
                    Imm.setPathImmagine(PathImmagine);

                    String CartellaImmagine = context.getFilesDir() + "/Player/" +
                            "/ImmaginiMusica/" + DatiBrano[3] + "/" + Imm2[1];
                    Imm.setCartellaImmagine(CartellaImmagine);

                    ListaImmagini.add(Imm);
                }
            }
            int immagine = -1;
            for (int i = 0; i < ListaImmagini.size(); i++) {
                if (ListaImmagini.get(i).getAlbum().toUpperCase().contains(s.getAlbum().toUpperCase().trim())) {
                    immagine = i;
                    break;
                }
            }
            if (immagine == -1) {
                immagine = UtilityPlayer.getInstance().GeneraNumeroRandom(ListaImmagini.size());
            }
            if (immagine > -1) {
                StrutturaImmagini si = ListaImmagini.get(immagine);
                ImmagineDaImpostare = si.getUrlImmagine();
                StruttImmDaImpostare = si;
            } else {
                // UtilityPlayer.getInstance().ImpostaSfondoLogo();
            }

            s.setImmagini(ListaImmagini);

            if (TestoEAltro[0].isEmpty()) {
                s.setAscoltata(0);
            } else {
                s.setAscoltata(Integer.parseInt(TestoEAltro[0]));
            }
            if (TestoEAltro[1].isEmpty()) {
                s.setBellezza(0);
            } else {
                s.setBellezza(Integer.parseInt(TestoEAltro[1]));
            }
            String Testo = "";
            if (TestoEAltro.length > 2) {
                Testo = TestoEAltro[2];
            } else {
                Testo = "";
            }
            if (!Testo.isEmpty()) {
                Testo = Testo.replace("§", "\n");
                Testo = Testo.replace("%20", " ");

                /* String NomeFile = Traccia + "-" + DatiBrano[5] + ".txt";
                if (!Files.getInstance().EsisteFile(CartellaTesto + NomeFile)) {
                    Files.getInstance().CreaCartelle(CartellaTesto);
                    Files.getInstance().ScriveFile(CartellaTesto, NomeFile, Testo);
                }
                NomeFile = Traccia + "-" + DatiBrano[5] + ".2.txt";
                if (!Files.getInstance().EsisteFile(CartellaTesto + NomeFile)) {
                    String Cosa = s.getBellezza() + ";" + s.getAscoltata();
                    Files.getInstance().CreaCartelle(CartellaTesto);
                    Files.getInstance().ScriveFile(CartellaTesto, NomeFile, Cosa);
                }
                NomeFile = Traccia + "-" + DatiBrano[5] + ".TAGS.txt";
                if (!Files.getInstance().EsisteFile(CartellaTesto + NomeFile)) {
                    String Cosa = s.getTags();
                    Files.getInstance().CreaCartelle(CartellaTesto);
                    Files.getInstance().ScriveFile(CartellaTesto, NomeFile, Cosa);
                }
                NomeFile = Traccia + "-" + DatiBrano[5] + ".DATA.txt";
                if (!Files.getInstance().EsisteFile(CartellaTesto + NomeFile)) {
                    String Cosa = s.getData();
                    Files.getInstance().CreaCartelle(CartellaTesto);
                    Files.getInstance().ScriveFile(CartellaTesto, NomeFile, Cosa);
                } */
            }
            s.setTesto(Testo);

            if (TestoEAltro.length > 3) {
                s.setTestoTradotto(TestoEAltro[3]);
            } else {
                s.setTestoTradotto("");
            }

            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "idBrano Pregresso: " + s.getIdBrano());
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Titolo Brano Pregresso: " + s.getBrano());
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Album Pregresso: " + s.getAlbum());
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Artista Pregresso: " + s.getArtista());

            // OggettiAVideo.getInstance().getImgPregresso().setVisibility(LinearLayout.VISIBLE);

            // UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Caricamento pregresso: " + branoPregresso);
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "File: " + s.getPathBrano());
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "File esistente: " + UtilityDetector.getInstance().EsisteFile(s.getPathBrano()));
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "URL: " + s.getUrlBrano());

            if (!UtilityDetector.getInstance().EsisteFile(s.getPathBrano())) {
                // DOWNLOAD MP3
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Scarico il brano in locale");
                new DownloadBrano(context, s).execute(s.getUrlBrano());
            } else {
                if (VariabiliStatichePlayer.getInstance().getUltimoBrano() == null) {
                    db_dati_player db = new db_dati_player(context);
                    db.ScriveUltimoBrano(s);

                    VariabiliStatichePlayer.getInstance().setUltimoBrano(s);
                }

                UtilityPlayer.getInstance().CaricaBranoNelLettore(context);
            }
        }
    }
}
