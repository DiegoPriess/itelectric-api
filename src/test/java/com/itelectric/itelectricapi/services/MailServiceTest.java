package com.itelectric.itelectricapi.services;

import com.itelectric.itelectricapi.service.MailService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MailService mailService;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        lenient().when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void createWelcomeEmail_ShouldReturnCorrectHtml() {
        String email = "diegopriess.dev@gmail.com";
        String password = "password123";

        String result = mailService.createWelcomeEmail(email, password);

        Assertions.assertTrue(result.contains(email));
        Assertions.assertTrue(result.contains(password));
        Assertions.assertTrue(result.contains("Bem-vindo ao iTeletric!"));
    }

    @Test
    void createNewBudgetEmail_ShouldReturnCorrectHtml() {
        String result = mailService.createNewBudgetEmail();

        Assertions.assertTrue(result.contains("Você tem um novo orçamento disponível em sua conta no ITElectric!"));
        Assertions.assertTrue(result.contains("Ver Orçamento"));
    }
}
