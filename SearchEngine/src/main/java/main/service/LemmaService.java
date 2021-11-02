package main.service;

import lombok.RequiredArgsConstructor;
import main.model.Lemma;
import main.repository.LemmaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LemmaService {

    private final LemmaRepository lemmaRepository;

    public void uploadLemmas(Collection<Lemma> lemmas) {
        lemmaRepository.saveAll(lemmas);
    }

    public List<Lemma> downloadLemmas() {
        return lemmaRepository.downloadLemmas();
    }

    public long countLemmas() {
        return lemmaRepository.count();
    }
}
