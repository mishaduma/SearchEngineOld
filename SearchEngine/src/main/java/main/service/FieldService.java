package main.service;

import lombok.RequiredArgsConstructor;
import main.model.Field;
import main.repository.FieldRepository;
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
        return fieldRepository.getFields();
    }
}
