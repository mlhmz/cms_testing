package de.szut.customer.database;

import de.szut.customer.database.model.CustomerEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    public CustomerRepository(CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
    }

    public boolean exists(final String customerName, final String companyName) {
        return customerJpaRepository.findFirstByNameAndAndCompany(customerName, companyName) != null;
    }

    public CustomerEntity store(final CustomerEntity customerEntity) {
        return customerJpaRepository.save(customerEntity);
    }

    public Optional<CustomerEntity> findBy(final long id) {
        return customerJpaRepository.findById(id);
    }
}
