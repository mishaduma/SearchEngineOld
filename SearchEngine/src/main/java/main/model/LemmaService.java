package main.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class LemmaService {

    private final LemmaRepository lemmaRepository;

    public void uploadLemmas(Collection<Lemma> lemmas) {
        lemmaRepository.saveAll(lemmas);
    }

    public ArrayList<Lemma> listLemmas() {
        ArrayList<Lemma> lemmas = new ArrayList<>();
        lemmaRepository.findAll().forEach(lemmas::add);
        return lemmas;
    }

}
