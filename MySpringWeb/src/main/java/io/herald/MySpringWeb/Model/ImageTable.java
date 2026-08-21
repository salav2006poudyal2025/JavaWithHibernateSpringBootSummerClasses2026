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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserTable user;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }


}
