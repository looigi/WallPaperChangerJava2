package com.looigi.wallpaperchanger2.classeGoogleDrive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.drive.model.File;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeGoogleDrive.UtilityGoogleDrive;

import java.util.List;

public class AdapterListenerFiles extends BaseAdapter {
    private Context context;
    private List<File> listaFiles;
    private LayoutInflater inflter;

    public AdapterListenerFiles(Context applicationContext, List<File> listaFiles) {
        this.context = applicationContext;
        this.listaFiles = listaFiles;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaFiles.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return ""; // Nessuna estensione trovata
        }
        return (fileName.substring(fileName.lastIndexOf('.') + 1)).toUpperCase().trim();
    }

    private void prendeIcona(ImageView img, String Estensione) {
        switch(Estensione) {
            case "JPG":
                img.setImageResource(R.drawable.jpg);
                break;
            case "PNG":
                img.setImageResource(R.drawable.png);
                break;
            case "APK":
                img.setImageResource(R.drawable.apk);
                break;
            default:
                img.setImageResource(R.drawable.unknown);
                break;
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.lista_files_google_drive, null);

        String Nome = listaFiles.get(i).getName();
        String id = listaFiles.get(i).getId();
        String Estensione = getFileExtension(Nome);

        TextView txtFile = view.findViewById(R.id.txtFile);

        txtFile.setText(Nome);

        ImageView imgFile = view.findViewById(R.id.imgFile);
        prendeIcona(imgFile, Estensione);
        imgFile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        return view;
    }
}
