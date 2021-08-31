package main.parser;

import lombok.RequiredArgsConstructor;
import main.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Parser {

    private final PageService pageService;
    private final FieldService fieldService;
    private final LemmaService lemmaService;
    private final SearchIndexService searchIndexService;
    private static volatile ArrayList<RankedLemma> rankedLemmas = new ArrayList<>();

    @Autowired
    private Sites sites;

    @PostConstruct
    public void run() throws IOException {

        //filling table "field"
        Field title = new Field();
        title.setName("title");
        title.setSelector("title");
        title.setWeight((float) 1.0);

        Field body = new Field();
        body.setName("body");
        body.setSelector("body");
        body.setWeight((float) 0.8);

        fieldService.uploadFields(Arrays.asList(title, body));

        //uploading pages
        for (Site site : sites.getSites()) {
            pageService.uploadPages(new ForkJoinPool().invoke(new Tasker(new Source(site.getUrl(), rankedLemmas, fieldService), pageService, fieldService, rankedLemmas)));
            System.out.println("Обработан сайт: " + site.getName());
        }
        List<Page> pages = pageService.listPages();
        System.out.println("Страниц добавлено: " + pages.size() + "\nЗаполняется таблица lemma...");

        Collection<Lemma> lemmas = new HashSet<>();
        Map<String, Integer> lemmasMap = rankedLemmas.stream().map(rankedLemma -> rankedLemma.getLemma()).collect(Collectors.toMap(Function.identity(), V -> 1, Integer::sum));
        for (String lemmaFromMap : lemmasMap.keySet()) {
            Lemma lemma = new Lemma();
            lemma.setLemma(lemmaFromMap);
            lemma.setFrequency(lemmasMap.get(lemmaFromMap).intValue());
            lemmas.add(lemma);
        }

        lemmaService.uploadLemmas(lemmas);

        ArrayList<Lemma> lemmasFromRepo = lemmaService.listLemmas();

        System.out.println("Лемм добавлено: " + lemmasFromRepo.size() + "\nЗаполняется таблица search_index...");

        Collection<SearchIndex> searchIndices = new HashSet<>();
        for (RankedLemma rankedLemma : rankedLemmas) {
            SearchIndex searchIndex = new SearchIndex();
            searchIndex.setLemmaId(lemmasFromRepo.stream().filter(lemma -> lemma.getLemma().equals(rankedLemma.getLemma())).findFirst().get().getId());
            searchIndex.setPageId(pages.stream().filter(page -> page.getPath().equals(rankedLemma.getUrl())).findFirst().get().getId());
            searchIndex.setLemmaRank(rankedLemma.getRank());
            searchIndices.add(searchIndex);
        }
        searchIndexService.uploadSearchIndex(searchIndices);

    }
}
