package com.looigi.wallpaperchanger2.acra;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;

import org.acra.ACRA;
import org.acra.BuildConfig;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.config.NotificationConfigurationBuilder;
import org.acra.config.ToastConfigurationBuilder;
import org.acra.data.StringFormat;

public class Acra extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        ACRA.init(this, new CoreConfigurationBuilder()
                //core configuration:
                .withBuildConfigClass(BuildConfig.class)
                .withReportFormat(StringFormat.JSON)
                // .withLogcatArguments("-t", "100", "MyApp:D", "*:S")
                .withPluginConfigurations(
                        new ToastConfigurationBuilder()
                                .withText(getString(R.string.toast_crash_report))
                                .build(),

                        new MailSenderConfigurationBuilder()
                                .withMailTo("looigi@gmail.com")
                                .withReportAsFile(true)
                                .withReportFileName("Crash.txt")
                                .withSubject("Errore " + VariabiliStaticheWallpaper.channelName)
                                .withBody("Descrizione errore nel file allegato")
                                .build(),

                        new NotificationConfigurationBuilder()
                                .withTitle(VariabiliStaticheWallpaper.channelName)
                                .withText("Errore nell'app")
                                .withChannelName(VariabiliStaticheWallpaper.channelName)
                                .withChannelImportance(NotificationManager.IMPORTANCE_MAX)
                                .withResIcon(R.drawable.ic_launcher)
                                .withSendButtonText("Invia")
                                .withResSendButtonIcon(R.drawable.icona_ok)
                                .withDiscardButtonText("Annulla")
                                .withResDiscardButtonIcon(R.drawable.elimina_quadrato)
                                // .withSendWithCommentButtonText(getString(R.string.notification_send_with_comment))
                                // .withResSendWithCommentButtonIcon(R.drawable.notification_send_with_comment)
                                // .withCommentPrompt(getString(R.string.notification_comment))
                                .withSendOnClick(false)
                                .build()
                ));


        /* if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code... */
    }
}