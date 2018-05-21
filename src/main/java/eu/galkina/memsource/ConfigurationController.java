package eu.galkina.memsource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import eu.galkina.memsource.model.Configuration;
import eu.galkina.memsource.repository.ConfigurationRepository;
import reactor.core.publisher.Mono;


@RestController
public class ConfigurationController {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @GetMapping("/configuration")
    public Mono<Configuration> getConfiguration() {
        List<Configuration> configurations = Lists.newArrayList(configurationRepository.findAll().iterator());
        if (configurations.size() ==0) {
            return Mono.empty();
        }
        if (configurations.size() > 1) {
            throw new IllegalStateException("Invalid configuration count " + configurations.size());
        }
        return Mono.just(configurations.get(0));
    }

    @PostMapping(path = "/updateConfiguration")
    public void updateConfiguration(@RequestBody Configuration configuration) {
        configurationRepository.deleteAll();
        configurationRepository.save(configuration);
    }


}
