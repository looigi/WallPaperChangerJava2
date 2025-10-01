package com.looigi.wallpaperchanger2.Player.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.Player.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.Player.UtilityPlayer;
import com.looigi.wallpaperchanger2.Player.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.Player.WebServices.ChiamateWsPlayer;

import java.util.List;

public class AdapterListenerBraniOnline extends BaseAdapter {
    private Context context;
    private List<StrutturaBrano> listaBrani;
    private LayoutInflater inflater;
    private String Filtro;

    public AdapterListenerBraniOnline(Context applicationContext, List<StrutturaBrano> Brani) {
        this.context = applicationContext;
        this.listaBrani = Brani;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaBrani.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.lista_brani_online, null);

        if (i < listaBrani.size()) {
            String NomeBrano = listaBrani.get(i).getBrano();
            String Artista = listaBrani.get(i).getArtista();
            String Album = listaBrani.get(i).getAlbum();
            String Traccia = listaBrani.get(i).getTraccia();

            /* ImageView imgElimina = view.findViewById(R.id.imgEliminaBrano);
            imgElimina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Si vuole eliminare l'eventuale brano in locale ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String Path = listaBrani.get(i).getPathBrano();
                            if (Files.getInstance().EliminaFileUnico(Path)) {
                                db_dati_player db = new db_dati_player(context);
                                db.EliminaBrano(String.valueOf(listaBrani.get(i).getIdBrano()));
                                listaBrani.remove(i);

                                notifyDataSetChanged();
                            } else {
                                UtilitiesGlobali.getInstance().ApreToast(context, "Files non eliminato");
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

                }
            }); */

            ImageView imgSuona = view.findViewById(R.id.imgSuonaBrano);
            imgSuona.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    StrutturaBrano s = listaBrani.get(i);
                    if (Files.getInstance().EsisteFile(s.getPathBrano())) {
                        VariabiliStatichePlayer.getInstance().setUltimoBrano(s);

                        UtilityPlayer.getInstance().CaricaBranoNelLettore(context);
                    } else {
                        if (VariabiliStatichePlayer.getInstance().getClasseChiamata() != null) {
                            VariabiliStatichePlayer.getInstance().getClasseChiamata().StoppaEsecuzione();
                        }
                        ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                        VariabiliStatichePlayer.getInstance().setClasseChiamata(ws);

                        ws.RitornaBranoDaID(String.valueOf(s.getIdBrano()), false);
                    }
                }
            });

            TextView tBrano = (TextView) view.findViewById(R.id.txtBrano);
            tBrano.setText(NomeBrano);

            TextView tTraccia = (TextView) view.findViewById(R.id.txtTraccia);
            tTraccia.setText(Traccia);
        }

        return view;
    }
}
