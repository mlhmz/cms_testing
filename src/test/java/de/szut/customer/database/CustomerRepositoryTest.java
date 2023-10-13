package de.szut.customer.database;

import de.szut.customer.database.model.CustomerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = {CustomerRepository.class})
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;
    @MockBean
    CustomerJpaRepository customerJpaRepository;

    @Test
    void exists() {
        CustomerEntity customerEntity = new CustomerEntity();
        doReturn(customerEntity).when(customerJpaRepository).findFirstByNameAndAndCompany(any(), any());

        boolean exists = customerRepository.exists("Customer", "Company");

        assertThat(exists).isTrue();
    }

    @Test
    void exists_isFalse_OnNonExistingCustomer() {
        doReturn(null).when(customerJpaRepository).findFirstByNameAndAndCompany(any(), any());

        boolean exists = customerRepository.exists("Customer", "Company");

        assertThat(exists).isFalse();
    }

    @Test
    void findBy() {
        CustomerEntity customerEntity = new CustomerEntity();
        doReturn(Optional.of(customerEntity)).when(customerJpaRepository).findById(any());

        Optional<CustomerEntity> result = customerRepository.findBy(1L);

        assertThat(result).isPresent();
    }

    @Test
    void findBy_IsNotPresent_OnNonExistingCustomer() {
        doReturn(Optional.empty()).when(customerJpaRepository).findById(any());

        Optional<CustomerEntity> result = customerRepository.findBy(1L);

        assertThat(result).isEmpty();
    }
}