package eu.galkina.memsource.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author Anna Galkina
 * Memsource project
 */
@Data
public class Project {

    private @Id Long id;

    private String name;
    private String status;
    private String sourceLang;
    private List<String> targetLangs;

}
