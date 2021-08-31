package main.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;

    public void uploadPages(Collection<Page> pages) {
        pageRepository.saveAll(pages);
    }

    public List<Page> listPages() {
        List<Page> pages = new ArrayList<>();
        pageRepository.findAll().forEach(pages::add);
        return pages;
    }
}
