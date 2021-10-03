package main.storage;

import lombok.RequiredArgsConstructor;
import main.model.Lemma;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
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

    public Lemma getLemma(String lemma) {

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Lemma targetLemma = sessionFactory.openSession().createQuery("from Lemma where lemma = '" + lemma + "'", Lemma.class).getResultList().get(0);

        sessionFactory.close();

        return targetLemma;
    }

    public List<Lemma> downloadLemmas() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        List<Lemma> lemmas = sessionFactory.openSession().createQuery("from Lemma", Lemma.class).getResultList();

        return lemmas;
    }

    public long countLemmas() {
        return lemmaRepository.count();
    }
}
