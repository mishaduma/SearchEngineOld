package main.parser;

import main.model.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class Tasker extends RecursiveTask<Collection<Page>> {

    private Source source;
    private Collection<Page> pages = new HashSet<>();

    public Tasker(Source source) {
        this.source = source;
    }

    @Override
    protected Collection<Page> compute() {

        List<Tasker> taskList = new ArrayList<>();
        for (Source child : source.getChildren()) {
            Tasker task = new Tasker(child);
            task.fork();
            taskList.add(task);
        }

        if (source.getPage().getPath() != null) {
            pages.add(source.getPage());
        }

        for (Tasker task : taskList) {
            pages.addAll(task.join());
        }
        return pages;
    }
}
