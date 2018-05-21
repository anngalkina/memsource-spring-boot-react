package eu.galkina.memsource.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author Anna Galkina
 * Memsource project
 */
@Data
@Entity
public class Project {

    private @Id Long id;

    private String name;

}
