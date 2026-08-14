package com.icms.emailservice.service;

import com.icms.emailservice.event.email.SendEmailEvent;

public interface EmailService {

    void sendMail(SendEmailEvent event);
}
