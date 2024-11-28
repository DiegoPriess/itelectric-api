package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendHtml(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new BusinessException("Erro ao enviar email");
        }
    }

    public String createWelcomeEmail(String email, String password) {
        return """
                <html>
                    <body style="font-family: Arial, sans-serif; margin: 0; padding: 0;">
                        <div style="background-color: #20494d; color: white; text-align: center; padding: 20px;">
                            <h1>Bem-vindo ao iTeletric!</h1>
                        </div>
                        <div style="padding: 20px;">
                            <p>Olá,</p>
                            <p>Estamos muito felizes em tê-lo conosco!</p>
                            <p>Você pode acessar nossa plataforma utilizando as seguintes credenciais:</p>
                            <ul style="list-style: none; padding: 0;">
                                <li><strong>Email:</strong> %s</li>
                                <li><strong>Senha:</strong> %s</li>
                            </ul>
                            <p style="margin-top: 20px;">Clique <a href="https://www.itelectric.com/entrar" style="color: #4CAF50;">aqui</a> para acessar o sistema.</p>
                            <p>Atenciosamente,</p>
                            <p><strong>Equipe ITElectric</strong></p>
                        </div>
                        <div style="background-color: #f1f1f1; text-align: center; padding: 10px;">
                            <p style="font-size: 12px; color: #888;">Este é um email automático. Por favor, não responda.</p>
                        </div>
                    </body>
                </html>
                """.formatted(email, password);
    }

    public String createNewBudgetEmail() {
        return """
                <html>
                    <body style="font-family: Arial, sans-serif; margin: 0; padding: 0;">
                        <div style="background-color: #20494d; color: white; text-align: center; padding: 20px;">
                            <h1>Olá!!!</h1>
                        </div>
                        <div style="padding: 20px;">
                            <p>Você tem um novo orçamento disponível em sua conta no ITElectric!</p>
                            <p>Para visualizar os detalhes do orçamento, clique no botão abaixo:</p>
                            <div style="text-align: center; margin-top: 20px;">
                                <a href="https://www.itelectric.com/entrar" style="background-color: #20494d; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-size: 16px;">
                                    Ver Orçamento
                                </a>
                            </div>
                            <p style="margin-top: 20px;">Se você tiver dúvidas, entre em contato com nossa equipe de suporte.</p>
                            <p>Atenciosamente,</p>
                            <p><strong>Equipe ITElectric</strong></p>
                        </div>
                        <div style="background-color: #f1f1f1; text-align: center; padding: 10px;">
                            <p style="font-size: 12px; color: #888;">Este é um email automático. Por favor, não responda.</p>
                        </div>
                    </body>
                </html>
                """;
    }

}
