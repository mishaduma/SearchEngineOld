package main.parser;

import main.model.Page;
import main.model.PageService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class Tasker extends RecursiveTask<Collection<Page>> {

    private Source source;

    private PageService pageService;

    public Tasker(Source source, PageService pageService) {
        this.source = source;
        this.pageService = pageService;
    }

    @Override
    protected Collection<Page> compute() {

        Collection<Page> pages = new HashSet<>();

        List<Tasker> taskList = new ArrayList<>();
        for (Source child : source.getChildren()) {
            Tasker task = new Tasker(child, pageService);
            task.fork();
            taskList.add(task);
        }

        if (source.getPage().getPath() != null) {
            pages.add(source.getPage());
            if (pages.size() == 20) {
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
