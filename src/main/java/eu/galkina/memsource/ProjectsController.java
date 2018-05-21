package eu.galkina.memsource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import eu.galkina.memsource.dto.Auth;
import eu.galkina.memsource.model.Configuration;
import eu.galkina.memsource.model.Project;
import eu.galkina.memsource.repository.ConfigurationRepository;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author Anna Galkina
 *
 * Rest endpoints for working with Memsource projects
 */
@RestController
public class ProjectsController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectsController.class);

    private static final String AUTH_PATH = "https://cloud.memsource.com/web/api/v3/auth/login";
    private static final String PROJECTS_PATH = "https://cloud.memsource.com/web/api/v3/project/list";

    @Autowired
    private ConfigurationRepository configurationRepository;

    @GetMapping("/projects")
    public Flux<Project> projects() {
        logger.info("Retrieving projects");
        Configuration configuration = configurationRepository.findAll().iterator().next();
        return WebClient.create(AUTH_PATH)
                //get the authentication token which is required for further requests
                .get()
                .uri(builder -> builder
                        .queryParam("userName", configuration.getUsername())
                        .queryParam("password", configuration.getPassword())
                        .build())
                .accept(APPLICATION_JSON)
                .exchange()
                .onErrorMap(this::handleAuthTokenError)
                .flatMap(clientResponse ->  clientResponse.toEntity(Auth.class))
                .flatMapMany(responseEntity ->
                        WebClient.create(PROJECTS_PATH)
                                //get the projects using the access token
                                .get()
                                .uri(builder -> builder
                                        .queryParam("token", responseEntity.getBody().getToken())
                                        .build())
                                .attribute("token", responseEntity.getBody().getToken())
                                .accept(APPLICATION_JSON)
                                .exchange()
                                .onErrorMap(this::handleGetProjectsError)
                                .flatMapMany(clientResponse -> clientResponse.toEntityList(Project.class))
                                .flatMap(responseEntity2 -> Flux.fromIterable(responseEntity2.getBody())));
    }

    private Throwable handleAuthTokenError(Throwable e) {
        logger.error("Unable to get authentication token. ", e);
        return e;
    }

    private Throwable handleGetProjectsError(Throwable e) {
        logger.error("Unable to get projects. ", e);
        return e;
    }
}
