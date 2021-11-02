package main.repository;

import main.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {
    @Query(value = "select * from Page", nativeQuery = true)
    List<Page> downloadPages();
}
