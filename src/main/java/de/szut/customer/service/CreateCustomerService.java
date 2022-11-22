package de.szut.customer.service;

import de.szut.customer.database.CustomerRepository;
import de.szut.customer.database.model.CustomerEntity;
import de.szut.customer.service.validator.CompanyNameValidator;
import de.szut.customer.service.validator.CustomerNameValidator;
import org.springframework.stereotype.Service;

@Service
public class CreateCustomerService {

    private final CustomerNameValidator customerNameValidator;
    private final CompanyNameValidator companyNameValidator;

    private final CustomerRepository customerRepository;

    public CreateCustomerService(final CustomerNameValidator customerNameValidator,
                                 final CompanyNameValidator companyNameValidator,
                                 final CustomerRepository customerRepository) {
        this.customerNameValidator = customerNameValidator;
        this.companyNameValidator = companyNameValidator;
        this.customerRepository = customerRepository;
    }

    public CustomerEntity createCustomer(final String customerName, final String companyName) {
        if (!customerNameValidator.isValid(customerName)) {
            throw new IllegalArgumentException("Customer name '" + customerName + "' is not valid!");
        }

        if (!companyNameValidator.isValid(companyName)) {
            throw new IllegalArgumentException("Company name '" + companyName + "' is not valid!");
        }

        if (customerRepository.exists(customerName, companyName)) {
            throw new IllegalArgumentException("Customer '" + customerName + "' in company '" + companyName + "' already exists!");
        }

        return customerRepository.store(new CustomerEntity(customerName, companyName));
    }

}
