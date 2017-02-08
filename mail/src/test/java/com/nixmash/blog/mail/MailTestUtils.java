package com.nixmash.blog.mail;

import com.nixmash.blog.mail.dto.MailDTO;

/**
 * Created by daveburke on 5/4/16.
 */
public class MailTestUtils {

    public static MailDTO testMailDTO() {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setFrom("testdude@aol.com");
        mailDTO.setFromName("Test Dude");
        mailDTO.setBody("This is a message from test dude");
        return  mailDTO;
    }
}
