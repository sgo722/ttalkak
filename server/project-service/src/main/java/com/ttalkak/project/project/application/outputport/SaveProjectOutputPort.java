package com.ttalkak.project.project.application.outputport;

import com.ttalkak.project.project.domain.model.ProjectEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface SaveProjectOutputPort {

    ProjectEntity save(ProjectEntity projectEntity);

}
