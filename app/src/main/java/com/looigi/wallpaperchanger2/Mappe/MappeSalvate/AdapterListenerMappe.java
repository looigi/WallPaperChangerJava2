package com.looigi.wallpaperchanger2.Mappe.MappeSalvate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.io.File;
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
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    File f = new File(NomeFileMappa);
                    Uri uri = FileProvider.getUriForFile(context,
                            context.getApplicationContext().getPackageName() + ".provider",
                            f);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, NomeMappa);
                    i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
                    i.putExtra(Intent.EXTRA_STREAM,uri);
                    i.setType(UtilitiesGlobali.getInstance().GetMimeType(context, uri));
                    context.startActivity(Intent.createChooser(i,"Share immagine mappa salvata"));
                }
            });

            ImageView imgElimina = (ImageView) view.findViewById(R.id.imgEliminaMappa);
            imgElimina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Mappe");
                    builder.setMessage("Elimina mappa salvata: " + NomeMappa);

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
