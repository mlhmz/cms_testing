package de.szut.customer.service.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {CustomerNameValidator.class})
class CustomerNameValidatorIntegrationTest {
    @Autowired
    CustomerNameValidator customerNameValidator;

    @ParameterizedTest(name = "Die Abfrage ist {1} beim Namen {0}")
    @CsvSource(value = {",false", "ab,false", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZäöüÄÖÜ,true", "a,false", "1234,false"})
    void isValid(String customerName, boolean valid) {
        boolean result = customerNameValidator.isValid(customerName);
        assertThat(result).isEqualTo(valid);
    }

    @Test
    @DisplayName("Die Abfrage ist invalide bei über 100 Zeichen")
    void isValid_FailsOnOver100Characters() {
        boolean result = customerNameValidator.isValid("A".repeat(101));
        assertThat(result).isFalse();
    }
}