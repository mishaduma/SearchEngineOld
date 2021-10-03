package main.storage;

import main.model.SearchIndex;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchIndexRepository extends CrudRepository<SearchIndex, Integer> {
}
