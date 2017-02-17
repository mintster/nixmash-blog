package com.nixmash.blog.mail.components;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * Created by daveburke on 2/17/17.
 */
@Component
public class MailUI {

    @Resource(name = "mailMessageSource")
    private MessageSource messageSource;

    // region Message Functions

    public String getMessage(String code, Object... params) {
        Locale current = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, params, current);
    }

    // endregion
}
