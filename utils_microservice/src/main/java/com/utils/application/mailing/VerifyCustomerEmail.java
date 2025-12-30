package com.utils.application.mailing;

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
public class VerifyCustomerEmail {
    private static final Logger logger = LoggerFactory.getLogger(VerifyCustomerEmail.class);

    private  MailService mailService;
    @Autowired
    public VerifyCustomerEmail(MailService mailService) {
        this.mailService = mailService;
    }


    @Async
    @EventListener
    public void sendVerifyCustomerEmail(VerifyCustomerEvent event) {
        try {
            logger.info("preparing to sent email to {}",event.getEmailTo());

            if (event.getPrivilegeId() == 1) {
                logger.info(" email : {} is registered as Alcohol agent sending mail",event.getEmailTo());
                event.setSubject("Verify Your Alcohol Agent Account!");
                mailService.sendVerifyCustomerEmail(event.getEmailTo(), event.getSubject(), event.getEmailFrom(), "verify-alcohol-agents", event.getName(), event.getToken());
            }else if (event.getPrivilegeId() == 2){
                logger.info(" email : {} is registered as Alcohol agent Retail sending mail",event.getEmailTo());
                event.setSubject("Verify Your Alcohol Agent Retail Account!");
                mailService.sendVerifyCustomerEmail(event.getEmailTo(), event.getSubject() , event.getEmailFrom(), "verify-alcohol-agent-retails", event.getName(), event.getToken());
            }else if (event.getPrivilegeId() == 3){
                event.setSubject("Verify Your Alcohol Agent Artist Account!");
                logger.info(" email : {} is registered as Alcohol agent Artist sending mail",event.getEmailTo());
                mailService.sendVerifyCustomerEmail(event.getEmailTo(), event.getSubject(), event.getEmailFrom(), "verify-alcohol-agent-artists", event.getName(), event.getToken());
            }
            } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
