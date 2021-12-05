package com.example.telefarma.servlets;

import com.example.telefarma.beans.BClient;
import com.example.telefarma.beans.BPharmacy;
import com.example.telefarma.daos.DistrictDao;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailServlet {

    public static void sendMail(String recipient, String subject, String mssg) {
        System.out.println("Preparando mail...");

        String emailAddress = "telefarma.app@gmail.com";
        String password = "Telefarma2021";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); //TLS
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, password);
            }
        });

        try {
            MimeMessage message = prepareMessage(session, emailAddress, recipient, subject, mssg);
            Transport.send(message);
            System.out.println("Mail enviado a " + recipient);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static MimeMessage prepareMessage(Session session, String emailAddress, String recipient, String subject, String mssg)
            throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailAddress));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setContent(mssg, "text/html; charset=utf-8");
        return message;
    }

    public static String pharmacyRegMssg(BPharmacy pharmacy, String dominio) {
        DistrictDao districtDao = new DistrictDao();
        return "La farmacia <i><strong>" + pharmacy.getName() + "</strong></i> se ha registrado correctamente:" +
                "<br><br><i>&nbsp; &nbsp; Email</i>:&nbsp; &nbsp; &nbsp; &nbsp; " +
                pharmacy.getMail() +
                "<br><i>&nbsp; &nbsp; Dirección</i>:&nbsp; " +
                pharmacy.getAddress() +
                "<br><i>&nbsp; &nbsp; Distrito</i>:&nbsp; &nbsp; &nbsp;" +
                districtDao.obtenerDistritoPorId(pharmacy.getDistrict().getIdDistrict()).getName() +
                "<br><i>&nbsp; &nbsp; RUC</i>:&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;" +
                pharmacy.getRUC() +
                "<br><br><a href='" + dominio + "/'>" +
                "Ingresa</a> para poder añadir productos al catálogo.";
    }

    public static String pharmacyEditMssg(BPharmacy pharmacy) {
        DistrictDao districtDao = new DistrictDao();
        return "La información de <strong>" + pharmacy.getName() + "</strong> ha sido actualizada:" +
                "<br><br><i>&nbsp; &nbsp; Email</i>:&nbsp; &nbsp; &nbsp; &nbsp; " +
                pharmacy.getMail() +
                "<br><i>&nbsp; &nbsp; Dirección</i>:&nbsp; " +
                pharmacy.getAddress() +
                "<br><i>&nbsp; &nbsp; Distrito</i>:&nbsp; &nbsp; &nbsp;" +
                districtDao.obtenerDistritoPorId(pharmacy.getDistrict().getIdDistrict()).getName() +
                "<br><i>&nbsp; &nbsp; RUC</i>:&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;" +
                pharmacy.getRUC();
    }

    public static String pharmacyBanMssg(BPharmacy pharmacy) {
        return "Lo sentimos, un administrador ha bloqueado la farmacia: <strong>" + pharmacy.getName() + "</strong>." +
                "<br>El motivo ingresado es el siguiente: " +
                "<br>&nbsp; &nbsp; " + pharmacy.getBanReason() +
                "<br>La farmacia ya no estará disponible para los usuarios.";
    }

    public static String pharmacyUnbanMssg(BPharmacy pharmacy) {
        return "Felicidades, la farmacia <i><strong>" + pharmacy.getName() + "</strong></i> ha sido debloqueada." +
                "<br>Ahora volverá a estar disponible en el catálogo de Telefarma.";
    }

    public static String clientRegMssg(BClient client, String dominio) {
        DistrictDao districtDao = new DistrictDao();
        return "Hola, <strong>" + client.getName() + "</strong>" +
                "<br><br>!Te has registrado exitosamente en Telefarma¡" +
                "<br>Desde nuestra plataforma podrás pedir todos los medicamentos que necesites." +
                "<br><br><a href='" + dominio + "/'>" +
                "Ingresa</a> y mira las farmacias en "
                + districtDao.obtenerDistritoPorId(client.getDistrict().getIdDistrict()).getName() + ".";
    }

    public static String rstPassTokenMssg(String rol, String token, String dominio) {
        return "Haz click en el siguiente enlace para cambiar la contraseña:" +
                "<br><a href='" + dominio + "/?action=cambiarContrasenha&rol=" + rol + "&token=" + token +
                "'>Cambiar contraseña<a>" +
                "<br><i>Si no fuiste tú, ignora este mensaje.</i>";
    }

}
