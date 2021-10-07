package main.service;

import lombok.RequiredArgsConstructor;
import main.model.SearchIndex;
import main.repository.SearchIndexRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class SearchIndexService {

    private final SearchIndexRepository searchIndexRepository;

    public void uploadSearchIndex(Collection<SearchIndex> searchIndices) {
        searchIndexRepository.saveAll(searchIndices);
    }

    public long countSearchIndex() {
        return searchIndexRepository.count();
    }
}
