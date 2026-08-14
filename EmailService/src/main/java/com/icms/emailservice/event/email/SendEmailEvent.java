package com.icms.emailservice.event.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailEvent {

    private String to;
    private String subject;
    private String body;
}
