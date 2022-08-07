package com.example.madscalculator;

import java.util.ArrayList;

public class User {

    ArrayList<String> list;

    public User(ArrayList<String> list) {
        this.list = list;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }
}
