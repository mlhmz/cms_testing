package de.szut.customer;

import de.szut.customer.database.model.CustomerEntity;
import de.szut.customer.testcontainers.AbstractIntegrationTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HappyPathIT extends AbstractIntegrationTest {

    @Test
    void storeNewCustomer() throws Exception {
        final String content = """
            {
                "name": "Lars",
                "company": "Neusta"
            }
            """;

        final var contentAsString = this.mockMvc.perform(post("/api/").content(content).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("name", is("Lars")))
            .andExpect(jsonPath("company", is("Neusta")))
            .andReturn()
            .getResponse()
            .getContentAsString();


        final var id = Long.parseLong(new JSONObject(contentAsString).get("id").toString());

        final var loadedEntity = customerJpaRepository.findById(id);

        assertThat(loadedEntity).isPresent();
        assertThat(loadedEntity.get().getId()).isEqualTo(id);
        assertThat(loadedEntity.get().getName()).isEqualTo("Lars");
        assertThat(loadedEntity.get().getCompany()).isEqualTo("Neusta");
        assertThat(loadedEntity.get().getCreateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertThat(loadedEntity.get().getLastUpdateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
    }

    @Test
    void getCustomer() throws Exception {
        CustomerEntity customerEntity = new CustomerEntity("Max Mustermann", "Musterfirma");

        CustomerEntity savedEntity = customerJpaRepository.save(customerEntity);

        this.mockMvc.perform(get("/api/{id}", savedEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is(savedEntity.getId().toString())))
            .andExpect(jsonPath("name", is("Max Mustermann")))
            .andExpect(jsonPath("company", is("Musterfirma")));
    }
}
