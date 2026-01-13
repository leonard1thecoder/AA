package com.utils.application.mailing;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@NoArgsConstructor
@Service
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private  JavaMailSender mailSender;
    private  TemplateEngine templateEngine;

    @Autowired
    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendVerifyCustomerEmail(String to, String subject, String from, String htmlFileName, String name,String token) throws MessagingException {

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("email", to);
        context.setVariable("token", token);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        String htmlContent = templateEngine.process(htmlFileName, context);


        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);


        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        //helper.addInline("logoImage", new ClassPathResource("/static/images/logo.png"));
        mailSender.send(mimeMessage);

        logger.info("email was sent successfully to {}",to);
    }

    public void sendSubmitEnquiryEmail( String subject, String messageFrom, String htmlFileName, String name,String message) throws MessagingException {

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("email",  messageFrom);
        context.setVariable("message", message);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        String htmlContent = templateEngine.process(htmlFileName, context);


        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);


        helper.setFrom("softwareaa65@gmail.com");
        helper.setTo("leonard1thecoder@gmail.com");
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        //helper.addInline("logoImage", new ClassPathResource("/static/images/logo.png"));
        mailSender.send(mimeMessage);

        logger.info("email was sent successfully from {}",messageFrom);
    }


}
