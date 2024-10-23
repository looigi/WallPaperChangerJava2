package com.looigi.wallpaperchanger2.classeGps.MappeSalvate;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdapterListenerMappe extends BaseAdapter {
    private Context context;
    private List<String> Mappe;
    private LayoutInflater inflater;
    private String Path;

    public AdapterListenerMappe(Context applicationContext, List<String> Mappe) {
        this.context = applicationContext;
        this.Mappe = Mappe;
        inflater = (LayoutInflater.from(applicationContext));
        Path = context.getFilesDir() + "/Mappe/";
    }

    @Override
    public int getCount() {
        return Mappe.size();
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
        view = inflater.inflate(R.layout.lista_mappe_salvate, null);

        if (i < Mappe.size()) {
            String NomeMappa = Mappe.get(i);
            String NomeFileMappa = Path + NomeMappa;

            ImageView imgImmagine = (ImageView) view.findViewById(R.id.imgImmagine);
            Bitmap bitmap;
            if (Files.getInstance().EsisteFile(NomeFileMappa)) {
                bitmap = BitmapFactory.decodeFile(NomeFileMappa);
                imgImmagine.setImageBitmap(bitmap);
            } else {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mappe);
                imgImmagine.setImageBitmap(bitmap);
            }
            imgImmagine.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliStaticheGPS.getInstance().getImgMappa().setImageBitmap(bitmap);
                }
            });

            TextView tMappa = (TextView) view.findViewById(R.id.txtMappa);
            tMappa.setText(NomeMappa);

            ImageView imgCondividi = (ImageView) view.findViewById(R.id.imgCondividiMappa);
            imgCondividi.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                }
            });

            ImageView imgElimina = (ImageView) view.findViewById(R.id.imgEliminaMappa);
            imgElimina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Elimina mappa salvata: " + NomeMappa);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Files.getInstance().EliminaFileUnico(NomeFileMappa);
                            Mappe.remove(i);
                            notifyDataSetChanged();
                            UtilitiesGlobali.getInstance().ApreToast(context, "Mappa eliminata");
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
            });
        }

        return view;
    }
}
