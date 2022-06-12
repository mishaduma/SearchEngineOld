package main.parser;

import lombok.RequiredArgsConstructor;
import main.model.*;
import main.service.FieldService;
import main.service.LemmaService;
import main.service.PageService;
import main.service.SearchIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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
    private static final List<RankedLemma> rankedLemmas = new ArrayList<>();

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

        List<Field> fields = fieldService.getFields();

        //uploading pages
        CompletableFuture.supplyAsync(() -> {
            List<CompletableFuture<Void>> futureList = new ArrayList<>();
            for (Site site : sites.getSites()) {
                futureList.add(CompletableFuture.supplyAsync(() -> {
                    pageService.uploadPages(new ForkJoinPool().invoke(new Tasker(new Source(site.getUrl(), rankedLemmas, fields), pageService, fields, rankedLemmas)));
                    System.out.println("Обработан сайт: " + site.getName());
                    return null;
                }));
            }

            futureList.forEach(CompletableFuture::join);

            System.out.println("Страниц добавлено: " + pageService.countPages() + "\nЗаполняется таблица lemma...");

            //uploading lemmas
            Collection<Lemma> lemmas = new HashSet<>();
            Map<String, Integer> lemmasMap = rankedLemmas.stream().map(RankedLemma::getLemma).collect(Collectors.toMap(Function.identity(), V -> 1, Integer::sum));
            for (Map.Entry<String, Integer> lemmaFromMap : lemmasMap.entrySet()) {
                Lemma lemma = new Lemma();
                lemma.setLemma(lemmaFromMap.getKey());
                lemma.setFrequency(lemmaFromMap.getValue());
                lemmas.add(lemma);
            }

            lemmaService.uploadLemmas(lemmas);

            System.out.println("Лемм добавлено: " + lemmaService.countLemmas() + "\nЗаполняется lemmasId...");

            //uploading searchIndex
            Collection<SearchIndex> searchIndices = new HashSet<>();
            Map<String, Integer> lemmasId = new HashMap<>();
            for (Lemma lemma : lemmaService.downloadLemmas()) {
                lemmasId.put(lemma.getLemma(), lemma.getId());
            }
            System.out.println("Заполняется pagesId...");
            Map<String, Integer> pagesId = new HashMap<>();
            for (Page page : pageService.downloadPages()) {
                pagesId.put(page.getPath(), page.getId());
            }
            System.out.println("Заполняется таблица search_index...");
            for (RankedLemma rankedLemma : rankedLemmas) {
                SearchIndex searchIndex = new SearchIndex();
                searchIndex.setLemmaId(lemmasId.get(rankedLemma.getLemma()));
                searchIndex.setPageId(pagesId.get(rankedLemma.getUrl()));
                searchIndex.setLemmaRank(rankedLemma.getRank());
                searchIndices.add(searchIndex);
            }
            searchIndexService.uploadSearchIndex(searchIndices);

            System.out.println("SearchIndex добавлено: " + searchIndexService.countSearchIndex());
            return null;
        });
    }
}
