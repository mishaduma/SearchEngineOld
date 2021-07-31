package main.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PageService {
    private final PagesRepository pagesRepository;

    public void uploadPages(Collection<Page> pages) {
        pagesRepository.saveAll(pages);
    }

    public List<Page> listPages() {
        List<Page> pages = new ArrayList<>();
        pagesRepository.findAll().forEach(pages::add);
        return pages;
    }
}
