package com.uyenpham.diploma.demoservice;

import java.io.Serializable;

/**
 * Created by steadfast-macmini-05 on 11/13/17.
 */

public class Song implements Serializable{
    private String name;
    private int id;

    public Song(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Song() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
