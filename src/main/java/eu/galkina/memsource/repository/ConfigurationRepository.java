package eu.galkina.memsource.repository;

import org.springframework.data.repository.CrudRepository;

import eu.galkina.memsource.model.Configuration;

/**
 * @author Anna Galkina
 */
public interface ConfigurationRepository extends CrudRepository<Configuration, Long> {

}
