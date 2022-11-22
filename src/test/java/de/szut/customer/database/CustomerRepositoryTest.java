package de.szut.customer.database;

import de.szut.customer.database.model.CustomerEntity;
import de.szut.customer.testcontainers.AbstractDatabaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@Import(CustomerRepository.class)
class CustomerRepositoryTest extends AbstractDatabaseTest {

    @Autowired
    private CustomerRepository repository;

    @Test
    void exists() {
        final var customerName = "Lars";
        final var companyName = "Neusta";
        customerJpaRepository.save(new CustomerEntity(customerName, companyName));

        assertThat(repository.exists(customerName, companyName)).isTrue();
    }

    @Test
    void notExists() {
        assertThat(repository.exists("not", "existing")).isFalse();
    }

    @Test
    void store() {
        final var customerName = "Lars";
        final var companyName = "Neusta";
        final var customerEntity = new CustomerEntity(customerName, companyName);

        final var storedEntity = repository.store(customerEntity);

        assertThat(storedEntity.getId()).isNotNull();
        assertThat(storedEntity.getName()).isEqualTo(customerName);
        assertThat(storedEntity.getCompany()).isEqualTo(companyName);
        assertThat(storedEntity.getCreateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertThat(storedEntity.getLastUpdateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));

        final var loadedEntity = customerJpaRepository.findById(storedEntity.getId());

        assertThat(loadedEntity).isPresent();
        assertThat(loadedEntity.get().getId()).isEqualTo(storedEntity.getId());
        assertThat(loadedEntity.get().getName()).isEqualTo(customerName);
        assertThat(loadedEntity.get().getCompany()).isEqualTo(companyName);
        assertThat(loadedEntity.get().getCreateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertThat(loadedEntity.get().getLastUpdateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
    }
}