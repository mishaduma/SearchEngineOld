package main.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Lemma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String lemma;

    @Column(nullable = false)
    private int frequency;
}
