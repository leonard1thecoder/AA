package com.users.application.services;

import com.users.application.dtos.ContactFormRequest;
import com.users.application.entities.ContactForm;
import com.users.application.exceptions.InvalidArgumentException;
import com.users.application.repository.ContactFormRepository;
import com.utils.application.mailing.dto.SubmitFormEvent;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.users.application.validators.UsersFieldsDataValidator.*;
import static com.utils.application.ExceptionHandler.throwExceptionAndReport;

@Service
public class ContactFormService {
    private final Logger logger= LoggerFactory.getLogger(ContactFormService.class);
    private final ApplicationEventPublisher publisher;
    private final ContactFormRepository repository;

    public ContactFormService(ApplicationEventPublisher publisher, ContactFormRepository repository) {
        this.publisher = publisher;
        logger.info("check publisher");
        this.repository = repository;
    }

    // Save a new contact form submission
    public String save(ContactFormRequest request) {
        boolean status = true;
        var resolveIssue ="";
        if(!isValidEmail(request.getEmail())){
             status = false;
             resolveIssue = "Please insert your correct email";
        }

        if(!isValidName(request.getName())){
            status = false;
             resolveIssue = "Please insert your correct name";
        }

        if(!isValidName(request.getSurname())){
            status = false;
             resolveIssue = "Please insert your correct surname";
        }

        if(request.getMessage().trim().isEmpty()){
            status = false;
             resolveIssue = "Please insert message";
        }

        logger.info("form request : {}",request);

        if(status) {
            var submittedForm = Stream.of(request)
                    .parallel()
                    .map(s -> ContactForm
                            .builder()
                            .email(s.getEmail())
                            .name(s.getName())
                            .surname(s.getSurname())
                            .message(s.getMessage())
                            .sentDate(getInstance().formatDateTime(LocalDateTime.now()))
                            .build())
                    .toList().get(0);
            publisher.publishEvent(SubmitFormEvent
                    .builder()
                    .emailFrom(submittedForm.getEmail())
                    .name(submittedForm.getName())
                    .message(submittedForm.getMessage())
                    .Subject("Alcohol Agent Enquire Message By " + submittedForm.getName())
                    .build()
            );
            repository.save(submittedForm);
           return "Enquiry successfully submitted";
        }else{
            return resolveIssue;
        }


    }


}