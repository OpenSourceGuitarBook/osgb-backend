package com.osgb.backend;

import com.osgb.backend.jobs.BookCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class BackendApplication {
    @Value("${osgb.book.rootfolder}")
    private static String bookRootFolder;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
