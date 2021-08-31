package main.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class SearchIndexService {

    private final SearchIndexRepository searchIndexRepository;

    public void uploadSearchIndex(Collection<SearchIndex> searchIndices) {
        searchIndexRepository.saveAll(searchIndices);
    }
}
