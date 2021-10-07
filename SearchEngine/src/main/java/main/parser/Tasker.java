package main.parser;

import lombok.SneakyThrows;
import main.model.Field;
import main.model.Page;
import main.model.RankedLemma;
import main.service.PageService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class Tasker extends RecursiveTask<Collection<Page>> {

    private Source source;

    private PageService pageService;
    private List<Field> fields;
    private ArrayList<RankedLemma> rankedLemmas;

    public Tasker(Source source, PageService pageService, List<Field> fields, ArrayList<RankedLemma> rankedLemmas) {
        this.source = source;
        this.pageService = pageService;
        this.fields = fields;
        this.rankedLemmas = rankedLemmas;
    }

    @SneakyThrows
    @Override
    protected Collection<Page> compute() {

        Collection<Page> pages = new HashSet<>();

        List<Tasker> taskList = new ArrayList<>();
        for (Source child : source.getChildren()) {
            Tasker task = new Tasker(child, pageService, fields, rankedLemmas);
            task.fork();
            taskList.add(task);
        }

        if (source.getPage().getCode() != null && source.getPage().getContent() != null) {
            pages.add(source.getPage());
            if (pages.size() == 250) {
                pageService.uploadPages(pages);
                pages.clear();
            }
        }

        for (Tasker task : taskList) {
            pages.addAll(task.join());
        }

        return pages;
    }
}
