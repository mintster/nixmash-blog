package com.nixmash.blog.mail;

import com.nixmash.blog.jpa.common.ApplicationSettings;
import com.nixmash.blog.mail.common.MailSettings;
import com.nixmash.blog.mail.components.MailUI;
import com.nixmash.blog.mail.components.MailSender;
import com.nixmash.blog.mail.dto.MailDTO;
import com.nixmash.blog.mail.service.FmMailService;
import com.nixmash.blog.mail.service.FmMailServiceImpl;
import freemarker.template.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
public class MessageTests extends MailContext {

    private MailSender mockMailSender;
    private MailDTO mailDTO;

    private FmMailService mockMailService;
    private MailSettings mailSettings;
    private ApplicationSettings applicationSettings;
    private Configuration fm;
    private Environment environment;

    @Autowired
    MailUI mailUI;

    @Before
    public void setUp() {
        mockMailSender = mock(MailSender.class);
        mockMailService =
                new FmMailServiceImpl(mockMailSender, mailSettings, applicationSettings, fm, environment, mailUI);
        mailDTO = MailTestUtils.testMailDTO();
    }

    @Test
    public void messageRetrieved() {
        assertTrue(mailUI.getMessage("test.me").contains("mode!"));
    }

    @Test
    public void convertMailTypeMessageStringToEnum() throws Exception {
        MailDTO.Type mailType = MailDTO.Type.HTML;
        String mailTypeString = mailUI.getMessage("mail.contact.body.type");
        assertEquals(mailTypeString, "HTML");

        mailType = Enum.valueOf(MailDTO.Type.class, mailTypeString);
        assertEquals(mailType, MailDTO.Type.HTML);

        mailTypeString = "PLAIN";
        mailType = Enum.valueOf(MailDTO.Type.class, mailTypeString);
        assertEquals(mailType, MailDTO.Type.PLAIN);

    }

}
