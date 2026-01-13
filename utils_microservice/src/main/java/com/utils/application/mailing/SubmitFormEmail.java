package com.utils.application.mailing;

import com.utils.application.mailing.dto.SubmitFormEvent;
import com.utils.application.mailing.dto.VerifyCustomerEvent;
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
public class SubmitFormEmail {
    private static final Logger logger = LoggerFactory.getLogger(SubmitFormEmail.class);

    private  MailService mailService;
    @Autowired
    public SubmitFormEmail(MailService mailService) {
        this.mailService = mailService;
    }


    @Async
    @EventListener
    public void sendVerifyCustomerEmail(SubmitFormEvent event) {
        try {
            logger.info("preparing to sent email to {}",event.getEmailTo());

                logger.info(" email : {} is registered as Alcohol agent sending mail",event.getEmailTo());

                mailService.sendSubmitEnquiryEmail( event.getSubject(), event.getEmailFrom(), "submit-enquiry", event.getName(), event.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
