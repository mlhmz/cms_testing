package de.szut.customer.rest;

import de.szut.customer.database.CustomerRepository;
import de.szut.customer.database.model.CustomerEntity;
import de.szut.customer.rest.dto.AddCustomerDto;
import de.szut.customer.rest.dto.ReadCustomerDto;
import de.szut.customer.service.CreateCustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private final CustomerRepository customerRepository;

    private final CreateCustomerService createCustomerService;

    public CustomerController(final CustomerRepository customerRepository,
                              final CreateCustomerService createCustomerService) {
        this.customerRepository = customerRepository;
        this.createCustomerService = createCustomerService;
    }

    @PostMapping("/")
    public ReadCustomerDto createCustomer(@RequestBody final AddCustomerDto customer) {
        final var entity = this.createCustomerService.createCustomer(customer.getName(), customer.getCompany());

        return mapToDto(entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadCustomerDto> getCustomer(@PathVariable final String id) {
        final var optionalEntity = this.customerRepository.findBy(Long.parseLong(id));
        if (optionalEntity.isPresent()) {
            final var entity = optionalEntity.get();
            return ResponseEntity.ok(mapToDto(entity));
        }

        return ResponseEntity.notFound().build();
    }

    private ReadCustomerDto mapToDto(final CustomerEntity entity) {
        return new ReadCustomerDto(entity.getId().toString(), entity.getName(), entity.getCompany());
    }
}
