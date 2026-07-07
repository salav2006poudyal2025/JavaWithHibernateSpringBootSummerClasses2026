package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//@ - annotations -> these help in getting all the required functions
// for any dependencies operations
@Entity
public class UserClass {
    //model according to MVC
    //model -> database columns

    //dependency JPA utilized for hibernate programming
    //JPA - Java Persistence API
    //hibernate utilize jpa's api for implementing databases
    //queries = jpa uses ideas for table and column operations

    @Id  //primary key
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;
    private String fullName;


    //getter and setter


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
