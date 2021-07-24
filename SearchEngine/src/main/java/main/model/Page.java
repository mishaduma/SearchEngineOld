package main.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(indexes = {@Index(columnList = "path")})
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(length = 200)
    private String path;

    @NotNull
    private Integer code;

    @NotNull
    @Column(columnDefinition = "mediumtext")
    private String content;
}
