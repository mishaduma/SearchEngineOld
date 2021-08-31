package main.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class SearchIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "page_id", nullable = false)
    private int pageId;

    @Column(name = "lemma_id", nullable = false)
    private int lemmaId;

    @Column(name = "lemma_rank", nullable = false)
    private float lemmaRank;
}
