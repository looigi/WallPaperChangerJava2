package com.looigi.wallpaperchanger2.Onomastici;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

import com.looigi.wallpaperchanger2.Onomastici.db.db_dati_compleanni;
import com.looigi.wallpaperchanger2.Onomastici.strutture.StrutturaCompleanno;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GestioneRubrica {

	public String PrendeNumeroCellulareDaStringa(ContentResolver Rubrica, String name){
        String number = "";
        String name1="";
        String id2="";
        int id;
        		
        Cursor cursor = Rubrica.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (cursor.getCount() > 0)
		{
		    while (cursor.moveToNext()) {
	            name1 = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	            if (name1.toUpperCase().trim().contains(name.toUpperCase().trim())) {
	            	int QualeCampo=cursor.getColumnIndex(BaseColumns._ID);
		            id = cursor.getInt(QualeCampo);
		            id2=(String) ""+id;
		            number=PrendeNumeroDaID(Rubrica, id2);
		            break;
	            }
		    }
		}
		cursor.close(); 
		
        return number;
	}

	public int VedeQuanteRicorrenze(ContentResolver Rubrica, String name){
        String name1="";
        int Quante=0;
        
        Cursor cursor = Rubrica.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (cursor.getCount() > 0)
		{
		    while (cursor.moveToNext()) {
	            name1 = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	            if (name1.toUpperCase().trim().contains(name.toUpperCase().trim())) {
	            	Quante++;
	            }
		    }
		}
		cursor.close();     
		
        return Quante;
	}
	
	private String PrendeNumeroDaID(ContentResolver Rubrica, String id){
		String Numero="";
		String Trovato="";
		// String Prefisso="";
		String TipoTelefono="";
		
		Uri myPhoneUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, id);
		Cursor phones = Rubrica.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
		while (phones.moveToNext()) {
			Numero = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			TipoTelefono=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
			if (TipoTelefono.equals("2")) {
				Trovato=Numero;
				break;
			// } else {
				// Il numero non ? cellulare, lascio perdere
				/* Numero=Numero.replace("+39", "");
				Numero=Numero.trim();
				Numero=Numero.replace("(","");
				Numero=Numero.replace(")","");
				Prefisso=Numero.substring(0, 2); */
			}
		}
 
		return Trovato;
	}
	
    public Bitmap retrieveContactPhoto(Long contactID) {
        Bitmap photo = null;
 
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(VariabiliStaticheOnomastici.getInstance().getContext().getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));
 
            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                /* ImageView imageView = (ImageView) findViewById(R.id.img_contact);
                imageView.setImageBitmap(photo); */
            }
 
            assert inputStream != null;
            inputStream.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }

        return photo;
    }

	public List<String> RitornaTuttiINomi(Context context, ContentResolver Rubrica){
		List<String> nomiRilevati=new ArrayList<String>();  
        String name1="";
        // String Nomi[] = null;
        		
        Cursor cursor = Rubrica.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (cursor.getCount() > 0)
		{
			db_dati_compleanni db = new db_dati_compleanni(context);
		    while (cursor.moveToNext()) {
	            name1 = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				String Nome = "";
				String Cognome = "";

				if (name1.contains(" ")) {
					String[] n = name1.split(" ");
					Nome = n[0];
					Cognome = name1.replace(Nome + " ", "");
				} else {
					Nome = name1;
				}

				StrutturaCompleanno s = new StrutturaCompleanno();
				s.setCognome(Cognome);
				s.setNome(Nome);
				s.setGiorno(0);
				s.setMese(0);
				s.setAnno(0);

				db.ScriveCompleanno(s);

				nomiRilevati.add(name1);
		    }
			db.ChiudeDB();
		}
		cursor.close();

		return nomiRilevati;
	}
}

