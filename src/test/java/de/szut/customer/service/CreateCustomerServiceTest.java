package de.szut.customer.service;

import de.szut.customer.database.CustomerRepository;
import de.szut.customer.database.model.CustomerEntity;
import de.szut.customer.service.validator.CompanyNameValidator;
import de.szut.customer.service.validator.CustomerNameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateCustomerServiceTest {

    final CustomerNameValidator customerNameValidatorMock = mock(CustomerNameValidator.class);
    final CompanyNameValidator companyNameValidatorMock = mock(CompanyNameValidator.class);
    final CustomerRepository customerRepositoryMock = mock(CustomerRepository.class);

    final CreateCustomerService service = new CreateCustomerService(customerNameValidatorMock, companyNameValidatorMock, customerRepositoryMock);

    @BeforeEach
    void setUp() {
        when(customerRepositoryMock.store(any())).thenAnswer(parameter -> parameter.getArguments()[0]);
    }

    @Test
    void customerNameIsInvalid() {
        final var customerName = "Lars";
        final var companyName = "Neusta";
        when(customerNameValidatorMock.isValid(customerName)).thenReturn(false);
        when(companyNameValidatorMock.isValid(companyName)).thenReturn(true);
        when(customerRepositoryMock.exists(customerName, companyName)).thenReturn(false);

        assertThatThrownBy(() -> service.createCustomer(customerName, companyName))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Customer name '" + customerName + "' is not valid!");
    }

    @Test
    void companyNameIsInvalid() {
        final var customerName = "Lars";
        final var companyName = "Neusta";
        when(customerNameValidatorMock.isValid(customerName)).thenReturn(true);
        when(companyNameValidatorMock.isValid(companyName)).thenReturn(false);
        when(customerRepositoryMock.exists(customerName, companyName)).thenReturn(false);

        assertThatThrownBy(() -> service.createCustomer(customerName, companyName))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Company name '" + companyName + "' is not valid!");
    }

    @Test
    void customerAlreadyExists() {
        final var customerName = "Lars";
        final var companyName = "Neusta";
        when(customerNameValidatorMock.isValid(customerName)).thenReturn(true);
        when(companyNameValidatorMock.isValid(companyName)).thenReturn(true);
        when(customerRepositoryMock.exists(customerName, companyName)).thenReturn(true);

        assertThatThrownBy(() -> service.createCustomer(customerName, companyName))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Customer '" + customerName + "' in company '" + companyName + "' already exists!");
    }

    @Test
    void customerNotExists() {
        final var customerName = "Lars";
        final var companyName = "Neusta";
        when(customerNameValidatorMock.isValid(customerName)).thenReturn(true);
        when(companyNameValidatorMock.isValid(companyName)).thenReturn(true);
        when(customerRepositoryMock.exists(customerName, companyName)).thenReturn(false);

        final var customerEntity = service.createCustomer("Lars", "Neusta");

        assertThat(customerEntity.getName()).isEqualTo("Lars");
        assertThat(customerEntity.getCompany()).isEqualTo("Neusta");
        assertThat(customerEntity.getCreateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertThat(customerEntity.getLastUpdateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));

        final var captor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerRepositoryMock).store(captor.capture());

        final var captoredEntity = captor.getValue();

        assertThat(captoredEntity.getName()).isEqualTo("Lars");
        assertThat(captoredEntity.getCompany()).isEqualTo("Neusta");
        assertThat(captoredEntity.getCreateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertThat(captoredEntity.getLastUpdateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
    }
}