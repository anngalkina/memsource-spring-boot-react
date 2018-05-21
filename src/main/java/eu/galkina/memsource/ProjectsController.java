package eu.galkina.memsource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import eu.galkina.memsource.dto.Auth;
import eu.galkina.memsource.model.Configuration;
import eu.galkina.memsource.model.Project;
import eu.galkina.memsource.repository.ConfigurationRepository;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
public class ProjectsController {

    private static final String AUTH_PATH = "https://cloud.memsource.com/web/api/v3/auth/login";
    private static final String PROJECTS_PATH = "https://cloud.memsource.com/web/api/v3/project/list";

    @Autowired
    private ConfigurationRepository configurationRepository;

    //TODO validation of the response
    @GetMapping("/projects")
    public Flux<Project> projects() {
        Configuration configuration = configurationRepository.findAll().iterator().next();
        return WebClient.create(AUTH_PATH)
                .get()
                .uri(builder -> builder
                        .queryParam("userName", configuration.getUsername())
                        .queryParam("password", configuration.getPassword())
                        .build())
                .accept(APPLICATION_JSON)
                .exchange()
                .flatMap(clientResponse ->  clientResponse.toEntity(Auth.class))
                .flatMapMany(responseEntity ->
                        WebClient.create(PROJECTS_PATH)
                                .get()
                                .uri(builder -> builder
                                        .queryParam("token", responseEntity.getBody().getToken())
                                        .build())
                                .attribute("token", responseEntity.getBody().getToken())
                                .accept(APPLICATION_JSON)
                                .exchange()
                                .flatMapMany(clientResponse -> clientResponse.toEntityList(Project.class))
                                .flatMap(responseEntity2 -> Flux.fromIterable(responseEntity2.getBody())));
    }
}
