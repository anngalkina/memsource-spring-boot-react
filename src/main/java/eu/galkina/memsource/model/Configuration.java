package eu.galkina.memsource.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author Anna Galkina
 * Memsource user configuration
 */
@Data
@Entity
public class Configuration {

    private @Id @GeneratedValue Long id;
    private String username;
    private String password;

}
