package main.service;

import lombok.RequiredArgsConstructor;
import main.model.Field;
import main.repository.FieldRepository;
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
public class FieldService {

    private final FieldRepository fieldRepository;

    public void uploadFields(Collection<Field> fields) {
        fieldRepository.saveAll(fields);
    }

    public List<Field> getFields() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        List<Field> fields = sessionFactory.openSession().createQuery("from Field", Field.class).getResultList();

        sessionFactory.close();

        return fields;
    }
}
