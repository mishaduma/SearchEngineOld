package main.service;

import lombok.RequiredArgsConstructor;
import main.model.Page;
import main.repository.PageRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;

    public void uploadPages(Collection<Page> pages) {
        pageRepository.saveAll(pages);
    }

    public List<Page> downloadPages() {
        return pageRepository.downloadPages();
    }

    public long countPages() {
        return pageRepository.count();
    }
}
