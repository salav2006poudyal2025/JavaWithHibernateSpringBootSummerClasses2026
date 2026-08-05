package io.herald.MySpringWeb.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ImageTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Lob //Large Objects
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;




}
