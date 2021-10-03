package main.storage;

import lombok.RequiredArgsConstructor;
import main.model.Field;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<Field> list = new ArrayList<>();
        fieldRepository.findAll().forEach(list::add);
        return list;
    }
}
