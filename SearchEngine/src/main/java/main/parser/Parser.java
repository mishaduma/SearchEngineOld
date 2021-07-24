package main.parser;

import lombok.RequiredArgsConstructor;
import main.model.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class Parser {

    private final PageService pageService;

    @Autowired
    private Sites sites;

    private int linksCount;

    @PostConstruct
    public void run() {
        for (Site site : sites.getSites()) {
            linksCount += pageService.findPages(site.getUrl());
            System.out.println("Обработан сайт: " + site.getName());
        }
        System.out.println("Ссылок обработано: " + linksCount);
    }
}
