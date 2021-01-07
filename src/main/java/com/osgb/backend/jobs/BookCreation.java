package com.osgb.backend.jobs;

import com.osgb.backend.models.Paragraph;
import com.osgb.backend.models.Section;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.osgb.backend.models.Chapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookCreation implements
        ApplicationListener<ContextRefreshedEvent> {

    public static final String TITLE_SEPARATOR = "-";
    @Value("${osgb.book.rootfolder}")
    private String root;

    private List<String> listAllFiles(String path) {
        List<String> ret = new ArrayList<>();

        if (isPathDirectory(path)) {
            String list[] = new File(path).list();

            if (list.length > 0) {
                ret = Arrays.asList(list.clone())
                        .stream()
                        .map(x -> String.join("/", path, x))
                        .collect(Collectors.toList());
            }
        }

        return ret;
    }

    private List<WrappedChapter> getChapters(List<String> folderContent) {
        return folderContent.stream()
                .filter(fileName -> isPathDirectory(fileName))
                .map(name -> {
                    WrappedChapter c = new WrappedChapter();
                    c.path = name;

                    if (name.contains(TITLE_SEPARATOR)) {
                        c.chapter.setTitle(name.substring(name.indexOf(TITLE_SEPARATOR) + 1).strip());
                        c.chapter.setIndex(name.substring(0, name.indexOf(TITLE_SEPARATOR)).strip());
                    } else {
                        c.chapter.setTitle(name);
                    }

                    return c;
                })
                .collect(Collectors.toList());
    }

    private List<Paragraph> getParagraphs(WrappedSection section) {
        return listAllFiles(section.path)
                .stream()
                .filter(fileName -> !isPathDirectory(fileName))
                .map(name -> {
                    Paragraph p = new Paragraph();

                    if (name.contains(TITLE_SEPARATOR)) {
                        p.setTitle(name.substring(name.indexOf(TITLE_SEPARATOR) + 1).strip());
                        p.setIndex(name.substring(0, name.indexOf(TITLE_SEPARATOR)).strip());
                    } else {
                        p.setTitle(name);
                    }

                    return p;
                })
                .collect(Collectors.toList());
    }

    private List<WrappedSection> getSections(WrappedChapter chapter) {

        return listAllFiles(chapter.path)
                .stream()
                .map(name -> {
                    WrappedSection s = new WrappedSection();
                    s.path = name;

                    if (name.contains(TITLE_SEPARATOR)) {
                        s.section.setTitle(name.substring(name.indexOf(TITLE_SEPARATOR) + 1).strip());
                        s.section.setIndex(name.substring(0, name.indexOf(TITLE_SEPARATOR)).strip());
                    } else {
                        s.section.setTitle(name);
                    }

                    return s;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // read all files and get the chapters
        List<WrappedChapter> chapters = getChapters(listAllFiles(root));

        // for each chapter list the files and get the sections
        for (WrappedChapter chapter : chapters) {
            List<WrappedSection> sections = getSections(chapter);

            // for each section list the files and get the paragraphs
            for (WrappedSection section : sections) {
                List<Paragraph> paragraphs = getParagraphs(section);

                // keep only the ones with paragraphs with all the extensions
                if(paragraphs.size() > 0) {
                    section.section.setParagraphs(new HashSet<>(paragraphs));
                    section.section.setChapter(chapter.chapter);
                }
            }
        }

        // build the data
        // insert in the DB
    }


    private boolean isPathDirectory(String myPath) {
        return new File(myPath).isDirectory();
    }

    private class WrappedChapter {
        public Chapter chapter;
        public String path;

        public WrappedChapter() {
            chapter = new Chapter();
        }
    }

    private class WrappedSection {
        public Section section;
        public String path;

        public WrappedSection() {
            section = new Section();
        }
    }
}
