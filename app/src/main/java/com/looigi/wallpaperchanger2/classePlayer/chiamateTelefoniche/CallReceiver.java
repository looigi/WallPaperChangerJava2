package com.looigi.wallpaperchanger2.classePlayer.chiamateTelefoniche;

import android.content.Context;
import android.widget.Toast;

import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.Date;

public class CallReceiver extends GestioneChiamate {
	private static final String NomeMaschera = "Call_Receiver";

    @Override
    protected void onIncomingCallStarted(String number, Date start) {
		if (!VariabiliStatichePlayer.getInstance().isPlayerAttivo()) {
			return;
		}

		Context context = UtilitiesGlobali.getInstance().tornaContextValido();
		UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Incoming Call Started: " + number);
    	if (VariabiliStatichePlayer.getInstance().isStaSuonando()) {
    		VariabiliStatichePlayer.getInstance().setStavaSuonando(1);
			UtilityPlayer.getInstance().PressionePlay(context, false);
    	} else {
			VariabiliStatichePlayer.getInstance().setStavaSuonando(0);
		}

		Toast.makeText(context, "Chiamata in arrivo iniziata", Toast.LENGTH_LONG).show();
	}

    @Override
    protected void onOutgoingCallStarted(String number, Date start) {
		if (!VariabiliStatichePlayer.getInstance().isPlayerAttivo()) {
			return;
		}

		Context context = UtilitiesGlobali.getInstance().tornaContextValido();
		UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Outgoing Call Started: " + number);
		if (VariabiliStatichePlayer.getInstance().isStaSuonando()) {
			VariabiliStatichePlayer.getInstance().setStavaSuonando(1);
			UtilityPlayer.getInstance().PressionePlay(context, false);
		} else {
			VariabiliStatichePlayer.getInstance().setStavaSuonando(0);
		}

		Toast.makeText(context, "Chiamata in uscita iniziata", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onIncomingCallEnded(String number, Date start, Date end) {
		if (!VariabiliStatichePlayer.getInstance().isPlayerAttivo()) {
			return;
		}

		Context context = UtilitiesGlobali.getInstance().tornaContextValido();
		UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Incoming Call End: " + number);
		if (VariabiliStatichePlayer.getInstance().getStavaSuonando() == 1) {
			VariabiliStatichePlayer.getInstance().setStavaSuonando(0);
			UtilityPlayer.getInstance().PressionePlay(context, true);
    	}

		Toast.makeText(context, "Chiamata in arrivo terminata", Toast.LENGTH_LONG).show();
	}

    @Override
    protected void onOutgoingCallEnded(String number, Date start, Date end) {
		if (!VariabiliStatichePlayer.getInstance().isPlayerAttivo()) {
			return;
		}

		Context context = UtilitiesGlobali.getInstance().tornaContextValido();
		UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Outgoing Call Ended: " + number);
		if (VariabiliStatichePlayer.getInstance().getStavaSuonando() == 1) {
			VariabiliStatichePlayer.getInstance().setStavaSuonando(0);
			UtilityPlayer.getInstance().PressionePlay(context,true);
		}

		Toast.makeText(context, "Chiamata in uscita terminata", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onMissedCall(String number, Date start) {
		if (!VariabiliStatichePlayer.getInstance().isPlayerAttivo()) {
			return;
		}

		Context context = UtilitiesGlobali.getInstance().tornaContextValido();
		UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "On Missed Call: " + number);
		if (VariabiliStatichePlayer.getInstance().getStavaSuonando() == 1) {
			VariabiliStatichePlayer.getInstance().setStavaSuonando(0);
			UtilityPlayer.getInstance().PressionePlay(context, true);
		}

		Toast.makeText(context, "Chiamata persa", Toast.LENGTH_LONG).show();
    }
}