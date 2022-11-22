package de.szut.customer.service.validator;

import org.springframework.stereotype.Component;

/**
 * Ein Kundenname ist gültig, wenn er
 * -> aus mindestens 3 Zeichen besteht
 * -> aus maximal 100 Zeichen besteht
 * -> er nur aus Groß- oder Kleinbuchstaben (inkl. AÖÜß) besteht
 */
@Component
public class CustomerNameValidator {

    public boolean isValid(final String customerName) {
        return customerName != null && this.isAlpha(customerName) && customerName.length() >= 3 && customerName.length() <= 100;
    }

    private boolean isAlpha(final String text) {
        return text.matches("[a-zA-ZäÄöÖüÜß]+");
    }

}
