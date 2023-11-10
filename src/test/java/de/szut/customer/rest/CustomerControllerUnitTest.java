package de.szut.customer.rest;

import de.szut.customer.database.CustomerRepository;
import de.szut.customer.rest.dto.AddCustomerDto;
import de.szut.customer.rest.dto.ReadCustomerDto;
import de.szut.customer.service.CreateCustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerUnitTest {
    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    CreateCustomerService createCustomerService;

    @Autowired


    @Test
    void createCustomer() {
    }

    private void assertReadCustomerDtoEqualsAddCustomerDto(AddCustomerDto expected, ReadCustomerDto actual) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getCompany()).isEqualTo(expected.getCompany());
    }

    @Test
    void getCustomer() {

    }
}