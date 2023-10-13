package de.szut.customer.service.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {CompanyNameValidator.class})
class CompanyNameValidatorIntegrationTest {
    @Autowired
    private CompanyNameValidator companyNameValidator;

    @ParameterizedTest(name = "Die Abfrage ist {1} beim Namen {0}")
    @CsvSource(value = {",false", "ab,false", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZäöüÄÖÜ0123456789,true", "a,false", "?!?!,false"})
    void isValid(String customerName, boolean valid) {
        boolean result = companyNameValidator.isValid(customerName);
        assertThat(result).isEqualTo(valid);
    }

    @Test
    @DisplayName("Die Abfrage ist invalide bei über 800 Zeichen")
    void isValid_FailsOnOver100Characters() {
        boolean result = companyNameValidator.isValid("A".repeat(801));
        assertThat(result).isFalse();
    }
}