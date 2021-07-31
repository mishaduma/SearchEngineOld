package main.parser;

import lombok.RequiredArgsConstructor;
import main.model.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ForkJoinPool;

@Component
@RequiredArgsConstructor
public class Parser {

    private final PageService pageService;

    @Autowired
    private Sites sites;

    @PostConstruct
    public void run() {
        for (Site site : sites.getSites()) {
            pageService.uploadPages(new ForkJoinPool().invoke(new Tasker(new Source(site.getUrl()), pageService)));
            System.out.println("Обработан сайт: " + site.getName());
        }
        System.out.println("Страниц добавлено: " + pageService.listPages().size());
    }
}
