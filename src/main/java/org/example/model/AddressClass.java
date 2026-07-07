package org.example.model;

import jakarta.persistence.*;

@Entity
public class AddressClass {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String Town;

    @OneToOne
    @JoinColumn (name = "userId")
    private UserClass user;

    public UserClass getUser() {
        return user;
    }

    public void setUser(UserClass user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }
}
