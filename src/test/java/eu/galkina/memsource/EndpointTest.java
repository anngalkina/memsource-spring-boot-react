package eu.galkina.memsource;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import eu.galkina.memsource.model.Configuration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EndpointTest {

    @LocalServerPort
    private int port;

    private static final String BASE_URL = "http://localhost:";

    private WebTestClient webTestClient;

    @Autowired
    private ConfigurationController configurationController;

    @Autowired
    private ProjectsController projectsController;

    @Before
    public void setup() {
        this.webTestClient = WebTestClient
                .bindToServer()
                .baseUrl(BASE_URL + port)
                .build();
    }

    @Test
    public void test1ContextLoads() throws Exception {
        assertThat(configurationController).isNotNull();
        assertThat(projectsController).isNotNull();
    }

    @Test
    public void test2ShouldReturnEmptyConfiguration() throws Exception {
        webTestClient
                .get()
                .uri("/configuration")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }

    @Test
    public void test3TestUpdateConfiguration() throws Exception {
        Configuration testConfiguration = new Configuration();
        testConfiguration.setUsername("root");
        testConfiguration.setPassword("root");
        webTestClient
                .post()
                .uri("/updateConfiguration")
                .body(BodyInserters.fromObject(testConfiguration))
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        webTestClient
                .get()
                .uri("/configuration")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("root")
                .jsonPath("$.password").isEqualTo("root");

    }


}
