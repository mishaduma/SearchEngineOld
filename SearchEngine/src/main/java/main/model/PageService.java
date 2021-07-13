package main.model;

import lombok.RequiredArgsConstructor;
import main.parser.Source;
import main.parser.Tasker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
public class PageService {
    private final PagesRepository pagesRepository;
    private Collection<Page> pages;

    public int findPages(String url) {
        Source source = new Source(url);
        pages = new ForkJoinPool().invoke(new Tasker(source));
        for (Page page : pages) {
            pagesRepository.save(page);
        }
        return pages.size();
    }

    public List<Page> listPages() {
        List<Page> pages = new ArrayList<>();
        pagesRepository.findAll().forEach(pages::add);
        return pages;
    }
}
