package main.repository;

import main.model.Lemma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LemmaRepository extends JpaRepository<Lemma, Integer> {
    @Query(value = "select * from Lemma", nativeQuery = true)
    List<Lemma> downloadLemmas();
}
