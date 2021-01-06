package com.osgb.backend.controllers;

import com.osgb.backend.dao.ChapterRepository;
import com.osgb.backend.models.Chapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("book")
public class BookController {

    @Autowired
    private ChapterRepository chapterRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Chapter> getBook() {
        return chapterRepository.findAll();
    }
}
