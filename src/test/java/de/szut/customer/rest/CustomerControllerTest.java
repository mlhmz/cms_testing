package de.szut.customer.rest;

import de.szut.customer.database.CustomerRepository;
import de.szut.customer.database.model.CustomerEntity;
import de.szut.customer.service.CreateCustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {CustomerController.class})
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepositoryMock;

    @MockBean
    private CreateCustomerService createCustomerServiceMock;

    @Test
    void createCustomer() throws Exception {
        final var id = 1L;
        final var customerName = "Lars";
        final var companyName = "Neusta";

        when(createCustomerServiceMock.createCustomer(customerName, companyName)).thenReturn(new CustomerEntity(id, customerName, companyName));

        final String content = """
            {
                "name": "Lars",
                "company": "Neusta"
            }
            """;

        this.mockMvc.perform(post("/api/").content(content).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("id", is(Long.toString(id))))
            .andExpect(jsonPath("name", is(customerName)))
            .andExpect(jsonPath("company", is(companyName)));
    }

    @Test
    void getNotExistingCustomer() throws Exception {
        final var id = 1L;

        when(customerRepositoryMock.findBy(id)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/api/" + id))
            .andExpect(status().isNotFound());
    }
}