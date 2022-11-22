package de.szut.customer.service.validator;

import org.springframework.stereotype.Component;

/**
 * Ein Firmenname ist gültig, wenn er
 * -> aus mindestens 3 Zeichen besteht
 * -> aus maximal 800 Zeichen besteht
 * -> er nur aus Groß- oder Kleinbuchstaben (inkl. AÖÜß) oder Zahlen besteht
 */
@Component
public class CompanyNameValidator {

    public boolean isValid(final String companyName) {
        return companyName != null && this.isAlphaNumeric(companyName) && companyName.length() > 2 && companyName.length() <= 800;
    }

    private boolean isAlphaNumeric(final String text) {
        return text.matches("[0-9a-zA-ZäÄöÖüÜß]+");
    }

}
