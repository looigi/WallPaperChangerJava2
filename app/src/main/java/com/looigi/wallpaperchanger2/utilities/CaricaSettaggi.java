package com.looigi.wallpaperchanger2.utilities;

import android.content.Context;

import com.looigi.wallpaperchanger2.classeDetector.db_dati_detector;
import com.looigi.wallpaperchanger2.classeFetekkie.db_dati_fetekkie;
import com.looigi.wallpaperchanger2.classeFilms.db_dati_films;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.classeGps.db_dati_gps;
import com.looigi.wallpaperchanger2.classeImmagini.db_dati_immagini;
import com.looigi.wallpaperchanger2.classeVideo.db_dati_video;
import com.looigi.wallpaperchanger2.classePennetta.db_dati_pennetta;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.classeAvvio.db_debug;
import com.looigi.wallpaperchanger2.classeWallpaper.db_dati_wallpaper;

import java.util.ArrayList;

public class CaricaSettaggi {
    private static final String NomeMaschera = "CaricaSettaggi";

    private static CaricaSettaggi instance = null;

    private CaricaSettaggi() {
    }

    public static CaricaSettaggi getInstance() {
        if (instance == null) {
            instance = new CaricaSettaggi();
        }

        return instance;
    }

    private boolean caricateImpostazioniDebug = false;
    private boolean caricateImpostazioniDetector = false;
    private boolean caricateImpostazioniGPS = false;
    private boolean caricateImpostazioniImmagini = false;
    private boolean caricateImpostazioniPennetta = false;
    private boolean caricateImpostazioniFetekkie = false;
    private boolean caricateImpostazioniPlayer = false;
    private boolean caricateImpostazioniWallpaper = false;
    private boolean caricateImpostazioniVideo = false;
    private boolean caricateImpostazioniFilms = false;

    public boolean isCaricateImpostazioniFetekkie() {
        return caricateImpostazioniFetekkie;
    }

    public void setCaricateImpostazioniFetekkie(boolean caricateImpostazioniFetekkie) {
        this.caricateImpostazioniFetekkie = caricateImpostazioniFetekkie;
    }

    public boolean isCaricateImpostazioniFilms() {
        return caricateImpostazioniFilms;
    }

    public void setCaricateImpostazioniFilms(boolean caricateImpostazioniFilms) {
        this.caricateImpostazioniFilms = caricateImpostazioniFilms;
    }

    public boolean isCaricateImpostazioniVideo() {
        return caricateImpostazioniVideo;
    }

    public void setCaricateImpostazioniVideo(boolean caricateImpostazioniVideo) {
        this.caricateImpostazioniVideo = caricateImpostazioniVideo;
    }

    public boolean isCaricateImpostazioniDebug() {
        return caricateImpostazioniDebug;
    }

    public void setCaricateImpostazioniDebug(boolean caricateImpostazioniDebug) {
        this.caricateImpostazioniDebug = caricateImpostazioniDebug;
    }

    public boolean isCaricateImpostazioniDetector() {
        return caricateImpostazioniDetector;
    }

    public void setCaricateImpostazioniDetector(boolean caricateImpostazioniDetector) {
        this.caricateImpostazioniDetector = caricateImpostazioniDetector;
    }

    public boolean isCaricateImpostazioniGPS() {
        return caricateImpostazioniGPS;
    }

    public void setCaricateImpostazioniGPS(boolean caricateImpostazioniGPS) {
        this.caricateImpostazioniGPS = caricateImpostazioniGPS;
    }

    public boolean isCaricateImpostazioniImmagini() {
        return caricateImpostazioniImmagini;
    }

    public void setCaricateImpostazioniImmagini(boolean caricateImpostazioniImmagini) {
        this.caricateImpostazioniImmagini = caricateImpostazioniImmagini;
    }

    public boolean isCaricateImpostazioniPennetta() {
        return caricateImpostazioniPennetta;
    }

    public void setCaricateImpostazioniPennetta(boolean caricateImpostazioniPennetta) {
        this.caricateImpostazioniPennetta = caricateImpostazioniPennetta;
    }

    public boolean isCaricateImpostazioniPlayer() {
        return caricateImpostazioniPlayer;
    }

    public void setCaricateImpostazioniPlayer(boolean caricateImpostazioniPlayer) {
        this.caricateImpostazioniPlayer = caricateImpostazioniPlayer;
    }

    public boolean isCaricateImpostazioniWallpaper() {
        return caricateImpostazioniWallpaper;
    }

    public void setCaricateImpostazioniWallpaper(boolean caricateImpostazioniWallpaper) {
        this.caricateImpostazioniWallpaper = caricateImpostazioniWallpaper;
    }

    public void ScriveLog(Context context, String Maschera, String Log) {
        /* if (VariabiliStaticheStart.getInstance().getPercorsoDIRLog().isEmpty()) {
            generaPath(context);
        } */

        if (context != null) {
            if (VariabiliStaticheStart.getInstance().getLog() == null) {
                LogInterno l = new LogInterno(context, true);
                VariabiliStaticheStart.getInstance().setLog(l);
            }

            /* if (!UtilityDetector.getInstance().EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheStart.getInstance().getLog().PulisceFileDiLog();
            }

            if (EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) { */
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("CaricaSettaggi", Maschera,  Log);
            // }
        } else {

        }
    }

    public String CaricaImpostazioniGlobali(Context c, String daDove) {
        String ritorno = "OK";

        Context context = c;

        if (c == null) {
            c = UtilitiesGlobali.getInstance().tornaContextValido();
        }
        if (c != null) {
            ScriveLog(context, NomeMaschera, "Inizio lettura impostazioni");

            db_debug dbDeb = new db_debug(context);
            caricateImpostazioniDebug = true;
            if (dbDeb.DbAperto()) {
                if (dbDeb.CreazioneTabelle()) {
                    int ritDet = dbDeb.CaricaImpostazioni();
                    if (ritDet == -3 || ritDet == -4) {
                        dbDeb.ImpostaValoriDiDefault();
                        if (dbDeb.PulisceDati()) {
                            if (dbDeb.CreazioneTabelle()) {
                                if (!dbDeb.ScriveImpostazioni()) {
                                    ritorno = "Errore salvataggio impostazioni Debug";
                                    caricateImpostazioniDebug = false;
                                } else {
                                    dbDeb.CompattaDB();
                                }
                            } else {
                                ritorno = "Errore creazione tabelle Debug 2";
                                caricateImpostazioniDebug = false;
                            }
                        } else {
                            ritorno = "Errore pulizia tabelle Debug";
                            caricateImpostazioniDebug = false;
                        }
                    }
                } else {
                    ritorno = "Errore creazione tabelle Debug 1";
                    caricateImpostazioniDebug = false;
                }
            } else {
                ritorno = "Errore db non aperto Debug";
                caricateImpostazioniDebug = false;
            }
            dbDeb.ChiudeDB();

            caricateImpostazioniDetector = true;
            db_dati_detector dbDet = new db_dati_detector(context);
            if (dbDet.DbAperto()) {
                if (dbDet.CreazioneTabelle()) {
                    int ritDet = dbDet.CaricaImpostazioni();
                    if (ritDet == -3 || ritDet == -4) {
                        dbDet.ImpostaValoriDiDefault();
                        if (dbDet.PulisceDati(context, daDove)) {
                            if (dbDet.CreazioneTabelle()) {
                                if (!dbDet.ScriveImpostazioni(context, daDove)) {
                                    ritorno = "Errore salvataggio impostazioni Detector";
                                    caricateImpostazioniDetector = false;
                                } else {
                                    dbDet.CompattaDB();
                                }
                            } else {
                                ritorno = "Errore creazione tabelle Detector 2";
                                caricateImpostazioniDetector = false;
                            }
                        } else {
                            ritorno = "Errore pulizia tabelle Detector";
                            caricateImpostazioniDetector = false;
                        }
                    }
                } else {
                    ritorno = "Errore creazione tabelle Detector 1";
                    caricateImpostazioniDetector = false;
                }
            } else {
                ritorno = "Errore db non aperto Detector";
                caricateImpostazioniDetector = false;
            }
            dbDet.ChiudeDB();

            caricateImpostazioniGPS = true;
            db_dati_gps dbGPS = new db_dati_gps(context);
            if (dbGPS.DbAperto()) {
                if (dbGPS.CreazioneTabelle()) {
                    int ritGPS = dbGPS.CaricaImpostazioni(daDove);
                    if (ritGPS == -3 || ritGPS == -4) {
                        dbGPS.ImpostaValoriDiDefault();
                        if (dbGPS.PulisceDati()) {
                            if (dbGPS.CreazioneTabelle()) {
                                if (!dbGPS.ScriveImpostazioni()) {
                                    ritorno = "Errore salvataggio impostazioni GPS";
                                    caricateImpostazioniGPS = false;
                                } else {
                                    dbGPS.CompattaDB();
                                }
                            } else {
                                ritorno = "Errore creazione tabelle GPS 2";
                                caricateImpostazioniGPS = false;
                            }
                        } else {
                            ritorno = "Errore pulizia tabelle GPS";
                            caricateImpostazioniGPS = false;
                        }
                    }
                } else {
                    ritorno = "Errore creazione tabelle GPS 1";
                    caricateImpostazioniGPS = false;
                }

                int ritGP2 = dbGPS.CaricaPuntiDiSpegnimento();
                if (ritGP2 != 0) {
                    VariabiliStaticheGPS.getInstance().setListaPuntiDiSpegnimento(new ArrayList<>());
                }
            } else {
                ritorno = "Errore db non aperto GPS";
                caricateImpostazioniGPS = false;
            }
            dbGPS.ChiudeDB();

            caricateImpostazioniImmagini = true;
            db_dati_immagini dbImm = new db_dati_immagini(context);
            if (dbImm.DbAperto()) {
                if (dbImm.CreazioneTabelle()) {
                    int ritImm = dbImm.CaricaImpostazioni();
                    if (ritImm == -3 || ritImm == -4) {
                        dbImm.ImpostaValoriDiDefault();
                        if (dbImm.PulisceDati()) {
                            if (dbImm.CreazioneTabelle()) {
                                if (!dbImm.ScriveImpostazioni()) {
                                    ritorno = "Errore salvataggio impostazioni Immagini";
                                    caricateImpostazioniImmagini = false;
                                } else {
                                    dbImm.CompattaDB();
                                }
                            } else {
                                ritorno = "Errore creazione tabelle Immagini 2";
                                caricateImpostazioniImmagini = false;
                            }
                        } else {
                            ritorno = "Errore pulizia tabelle Immagini";
                            caricateImpostazioniImmagini = false;
                        }
                    }
                } else {
                    ritorno = "Errore creazione tabelle Immagini 1";
                    caricateImpostazioniImmagini = false;
                }
            } else {
                ritorno = "Errore db non aperto Immagini";
                caricateImpostazioniImmagini = false;
            }
            dbImm.ChiudeDB();

            caricateImpostazioniPennetta = true;
            db_dati_pennetta dbPen = new db_dati_pennetta(context);
            if (dbPen.DbAperto()) {
                if (dbPen.CreazioneTabelle()) {
                    int ritPen = dbPen.CaricaImpostazioni();
                    if (ritPen == -3 || ritPen == -4) {
                        dbPen.ImpostaValoriDiDefault();
                        if (dbPen.PulisceDati()) {
                            if (dbPen.CreazioneTabelle()) {
                                if (!dbPen.ScriveImpostazioni()) {
                                    ritorno = "Errore salvataggio impostazioni Pennetta";
                                    caricateImpostazioniPennetta = false;
                                } else {
                                    dbPen.CompattaDB();
                                }
                            } else {
                                ritorno = "Errore creazione tabelle Pennetta 2";
                                caricateImpostazioniPennetta = false;
                            }
                        } else {
                            ritorno = "Errore pulizia tabelle Pennetta";
                            caricateImpostazioniPennetta = false;
                        }
                    }
                } else {
                    ritorno = "Errore creazione tabelle Pennetta 1";
                    caricateImpostazioniPennetta = false;
                }
            } else {
                ritorno = "Errore db non aperto Pennetta";
                caricateImpostazioniPennetta = false;
            }
            dbPen.ChiudeDB();

            caricateImpostazioniFetekkie = true;
            db_dati_fetekkie dbFet = new db_dati_fetekkie(context);
            if (dbFet.DbAperto()) {
                if (dbFet.CreazioneTabelle()) {
                    int ritFet = dbFet.CaricaImpostazioni();
                    if (ritFet == -3 || ritFet == -4) {
                        dbFet.ImpostaValoriDiDefault();
                        if (dbFet.PulisceDati()) {
                            if (dbFet.CreazioneTabelle()) {
                                if (!dbFet.ScriveImpostazioni()) {
                                    ritorno = "Errore salvataggio impostazioni Fetekkie";
                                    caricateImpostazioniFetekkie = false;
                                } else {
                                    dbFet.CompattaDB();
                                }
                            } else {
                                ritorno = "Errore creazione tabelle Fetekkie 2";
                                caricateImpostazioniFetekkie = false;
                            }
                        } else {
                            ritorno = "Errore pulizia tabelle Fetekkie";
                            caricateImpostazioniFetekkie = false;
                        }
                    }
                } else {
                    ritorno = "Errore creazione tabelle Fetekkie 1";
                    caricateImpostazioniFetekkie = false;
                }
            } else {
                ritorno = "Errore db non aperto Fetekkie";
                caricateImpostazioniFetekkie = false;
            }
            dbFet.ChiudeDB();

            caricateImpostazioniVideo = true;
            db_dati_video dbVid = new db_dati_video(context);
            if (dbVid.DbAperto()) {
                if (dbVid.CreazioneTabelle()) {
                    int ritVid = dbVid.CaricaImpostazioni();
                    if (ritVid == -3 || ritVid == -4) {
                        dbVid.ImpostaValoriDiDefault();
                        if (dbVid.PulisceDati()) {
                            if (dbVid.CreazioneTabelle()) {
                                if (!dbVid.ScriveImpostazioni()) {
                                    ritorno = "Errore salvataggio impostazioni Video";
                                    caricateImpostazioniVideo = false;
                                } else {
                                    dbVid.CompattaDB();
                                }
                            } else {
                                ritorno = "Errore creazione tabelle Video 2";
                                caricateImpostazioniVideo = false;
                            }
                        } else {
                            ritorno = "Errore pulizia tabelle Video";
                            caricateImpostazioniVideo = false;
                        }
                    }
                } else {
                    ritorno = "Errore creazione tabelle Video 1";
                    caricateImpostazioniVideo = false;
                }
            } else {
                ritorno = "Errore db non aperto Video";
                caricateImpostazioniVideo = false;
            }
            dbVid.ChiudeDB();

            caricateImpostazioniFilms = true;
            db_dati_films dbFilms = new db_dati_films(context);
            if (dbFilms.DbAperto()) {
                if (dbFilms.CreazioneTabelle()) {
                    int ritFilms = dbFilms.CaricaImpostazioni();
                    if (ritFilms == -3 || ritFilms == -4) {
                        dbFilms.ImpostaValoriDiDefault();
                        if (dbFilms.PulisceDati()) {
                            if (dbFilms.CreazioneTabelle()) {
                                if (!dbFilms.ScriveImpostazioni()) {
                                    ritorno = "Errore salvataggio impostazioni Films";
                                    caricateImpostazioniFilms = false;
                                } else {
                                    dbFilms.CompattaDB();
                                }
                            } else {
                                ritorno = "Errore creazione tabelle Films 2";
                                caricateImpostazioniFilms = false;
                            }
                        } else {
                            ritorno = "Errore pulizia tabelle Films";
                            caricateImpostazioniFilms = false;
                        }
                    }
                } else {
                    ritorno = "Errore creazione tabelle Films 1";
                    caricateImpostazioniFilms = false;
                }
            } else {
                ritorno = "Errore db non aperto Films";
                caricateImpostazioniFilms = false;
            }
            dbFilms.ChiudeDB();

            caricateImpostazioniPlayer = true;
            db_dati_player dbPlay = new db_dati_player(context);
            if (dbPlay.DbAperto()) {
                if (dbPlay.CreazioneTabelle()) {
                    int ritPlay = dbPlay.CaricaImpostazioni();
                    if (ritPlay == -3 || ritPlay == -4) {
                        dbPlay.ImpostaValoriDiDefault();
                        if (dbPlay.PulisceDati()) {
                            if (dbPlay.CreazioneTabelle()) {
                                if (!dbPlay.ScriveImpostazioni()) {
                                    ritorno = "Errore salvataggio impostazioni Player";
                                    caricateImpostazioniPlayer = false;
                                } else {
                                    dbPlay.CompattaDB();
                                }
                            } else {
                                ritorno = "Errore creazione tabelle Player 2";
                                caricateImpostazioniPlayer = false;
                            }
                        } else {
                            ritorno = "Errore pulizia tabelle Player";
                            caricateImpostazioniPlayer = false;
                        }
                    }

                    int ritPlayR = dbPlay.CaricaRicerche();
                    if (ritPlayR < 0) {
                        ritorno = "Errore ricerche Player 2";
                    }
                } else {
                    ritorno = "Errore creazione tabelle Player 1";
                    caricateImpostazioniPlayer = false;
                }
            } else {
                ritorno = "Errore db non aperto Player";
                caricateImpostazioniPlayer = false;
            }
            dbPlay.ChiudeDB();

            caricateImpostazioniWallpaper = true;
            db_dati_wallpaper dbW = new db_dati_wallpaper(context);
            if (dbW.DbAperto()) {
                if (dbW.CreazioneTabelle()) {
                    int ritW = dbW.LeggeImpostazioni();
                    if (ritW == -2 || ritW == -3) {
                        if (dbW.PulisceDati()) {
                            if (dbW.CreazioneTabelle()) {
                                if (!dbW.ScriveImpostazioni()) {
                                    ritorno = "Errore salvataggio impostazioni Wallpaper";
                                    caricateImpostazioniWallpaper = false;
                                } else {
                                    dbW.CompattaDB();
                                }
                            } else {
                                ritorno = "Errore creazione tabelle Wallpaper 2";
                                caricateImpostazioniWallpaper = false;
                            }
                        } else {
                            ritorno = "Errore pulizia tabelle Wallpaper";
                            caricateImpostazioniWallpaper = false;
                        }
                    }
                } else {
                    ritorno = "Errore creazione tabelle Wallpaper 1";
                    caricateImpostazioniWallpaper = false;
                }
            } else {
                ritorno = "Errore apertura db Wallpaper";
                caricateImpostazioniWallpaper = false;
            }
            dbW.ChiudeDB();

            ScriveLog(context, NomeMaschera, "Fine lettura impostazioni: " + ritorno);
        } else {
            ritorno = "ERRORE. Context non valido";
        }

        return ritorno;
    }
}
