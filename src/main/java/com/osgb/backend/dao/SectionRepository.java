package com.osgb.backend.dao;

import com.osgb.backend.models.Section;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SectionRepository extends CrudRepository<Section, Long> {
}
