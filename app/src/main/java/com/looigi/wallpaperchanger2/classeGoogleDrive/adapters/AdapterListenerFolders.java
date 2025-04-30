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

public class AdapterListenerFolders extends BaseAdapter {
    private Context context;
    private List<File> listaFolders;
    private LayoutInflater inflter;

    public AdapterListenerFolders(Context applicationContext, List<File> listaFolders) {
        this.context = applicationContext;
        this.listaFolders = listaFolders;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaFolders.size();
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
        view = inflter.inflate(R.layout.lista_folders_google_drive, null);

        String Nome = listaFolders.get(i).getName();
        String id = listaFolders.get(i).getId();

        TextView txtFolder = view.findViewById(R.id.txtFolder);

        txtFolder.setText(Nome);

        ImageView imgFolder = view.findViewById(R.id.imgFolder);
        imgFolder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityGoogleDrive.getInstance().listaFolders(context, id);
            }
        });

        return view;
    }
}
