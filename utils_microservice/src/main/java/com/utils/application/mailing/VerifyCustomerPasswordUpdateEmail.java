package com.utils.application.mailing;

import com.utils.application.mailing.dto.VerifyCustomerEvent;
import com.utils.application.mailing.dto.VerifyUpdatePasswordEvent;
import jakarta.mail.MessagingException;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class VerifyCustomerPasswordUpdateEmail {
    private static final Logger logger = LoggerFactory.getLogger(VerifyCustomerPasswordUpdateEmail.class);
    private  MailService mailService;

    @Autowired
    public VerifyCustomerPasswordUpdateEmail(MailService mailService) {
        this.mailService = mailService;
    }

    @Async
    @EventListener
    public void sendVerifyPasswordUpdateEmail(VerifyUpdatePasswordEvent event) {
        try {
            logger.info("preparing to sent email to {} for verify password updating", event.getEmailTo());
                event.setSubject("Verify Alcohol Agent Password Update!");
                mailService.sendVerifyCustomerEmail(event.getEmailTo(), event.getSubject(), event.getEmailFrom(), "verify-alcohol-agents-password-update", event.getName(), event.getToken());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
