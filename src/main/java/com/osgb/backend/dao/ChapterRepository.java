package com.osgb.backend.dao;

import com.osgb.backend.models.Chapter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ChapterRepository extends CrudRepository<Chapter, Long> {
}
