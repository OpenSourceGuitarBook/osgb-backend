package com.osgb.backend.dao;

import com.osgb.backend.models.Paragraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ParagraphRepository extends CrudRepository<Paragraph, Long> {
}
