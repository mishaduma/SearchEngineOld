package main.service;

import lombok.RequiredArgsConstructor;
import main.model.Page;
import main.repository.PageRepository;
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
public class PageService {

    private final PageRepository pageRepository;

    public void uploadPages(Collection<Page> pages) {
        pageRepository.saveAll(pages);
    }

    public Page getPage(String url) {

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Page targetPage = sessionFactory.openSession().createQuery("from Page where path = '" + url + "'", Page.class).getResultList().get(0);

        sessionFactory.close();

        return targetPage;
    }

    public List<Page> downloadPages() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        List<Page> pages = sessionFactory.openSession().createQuery("from Page", Page.class).getResultList();

        sessionFactory.close();

        return pages;
    }

    public long countPages() {
        return pageRepository.count();
    }
}
