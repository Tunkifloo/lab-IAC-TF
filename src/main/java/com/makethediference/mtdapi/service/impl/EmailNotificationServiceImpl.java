package com.makethediference.mtdapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl {

    private final JavaMailSender mailSender;

    public void sendVolunteerApprovalEmail(String to, String username, String password, String role) {
        String subject = "Tu cuenta de voluntario ha sido aprobada";
        String textBody = String.format(
                "¡Felicidades! Tu solicitud de voluntariado para Make The Difference Girl Up ha sido aprobada.%n%n" +
                        "Se ha creado un usuario con las siguientes credenciales:%n" +
                        "Username: %s%n" +
                        "Password: %s%n" +
                        "Role: %s%n%n" +
                        "Por favor, inicia sesión y actualiza tu contraseña lo antes posible.%n" +
                        "¡Gracias por unirte a nuestro equipo!",
                username, password, role
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(textBody);
        mailSender.send(message);
    }
}
