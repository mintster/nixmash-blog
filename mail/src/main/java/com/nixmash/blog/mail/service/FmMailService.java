package com.nixmash.blog.mail.service;

import com.nixmash.blog.jpa.model.User;
import com.nixmash.blog.mail.dto.MailDTO;

/**
 * Created by daveburke on 4/28/16.
 */
public interface FmMailService {

    void sendResetPasswordMail(User user, String token);

    void sendContactMail(MailDTO mailDTO);

    void sendUserVerificationMail(User user);
}
