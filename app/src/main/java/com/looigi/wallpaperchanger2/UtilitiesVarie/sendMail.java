package com.looigi.wallpaperchanger2.UtilitiesVarie;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class sendMail {
    private final String senderEmail = "looigi@gmail.com";
    private final String senderPassword = "huht vrdb fgsk adoa"; // Usa OAuth o App Password!

    public void sendMail(String recipient, String subject, String body) throws MessagingException {
        String manufacturer = Build.MANUFACTURER;  // Marca (es. Samsung, Xiaomi)
        String model = Build.MODEL;               // Modello (es. Galaxy S23, Redmi Note 10)
        String device = Build.DEVICE;             // Nome interno del dispositivo
        String brand = Build.BRAND;               // Brand del produttore
        String product = Build.PRODUCT;           // Nome del prodotto

        if (manufacturer.equals("Google")) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String data = sdf.format(new Date());

        body = data + "\n--------------------------------------------\n\n" + body;
        body += "\n\n--------------------------------------------" +
                "\nMarca telefono: " + manufacturer +
                "\nModello: " + model +
                "\nDevice: " + device +
                "\nBrand: " + brand +
                "\nProdotto: " + product;

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
