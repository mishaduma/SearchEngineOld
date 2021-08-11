package main.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(indexes = {@Index(columnList = "path")})
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200, nullable = false)
    private String path;

    @Column(nullable = false)
    private Integer code;

    @Column(columnDefinition = "mediumtext", nullable = false)
    private String content;
}
