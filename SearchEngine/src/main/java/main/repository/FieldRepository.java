package main.repository;

import main.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<Field, Integer> {
    @Query(value = "select * from Field", nativeQuery = true)
    List<Field> getFields();
}
