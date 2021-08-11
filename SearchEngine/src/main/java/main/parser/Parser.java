package main.parser;

import lombok.RequiredArgsConstructor;
import main.lemmatizator.Lemma;
import main.model.PageService;
import main.model.Site;
import main.model.Sites;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

@Component
@RequiredArgsConstructor
public class Parser {

    private final PageService pageService;

    @Autowired
    private Sites sites;

    @PostConstruct
    public void run() throws IOException {

        Map<String, Integer> result = new Lemma().get("Повторное появление леопарда в Осетии позволяет предположить, что\n" +
                "леопард постоянно обитает в некоторых районах Северного Кавказа.");

        for (String word : result.keySet()) {
            System.out.println(word + " - " + result.get(word).intValue());
        }
        /*for (Site site : sites.getSites()) {
            pageService.uploadPages(new ForkJoinPool().invoke(new Tasker(new Source(site.getUrl()), pageService)));
            System.out.println("Обработан сайт: " + site.getName());
        }
        System.out.println("Страниц добавлено: " + pageService.listPages().size());*/
    }
}
