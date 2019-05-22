package com.example.appserverpokemon.model;

import java.util.ArrayList;

public class Partida {

    private String id;
    private String name;
    private int initialMoney;
    private ArrayList<UserGame> users;


    public Partida(){

    }

    public Partida(String id, String name, int initialMoney, ArrayList<UserGame> users){
        this.id = id;
        this.name = name;
        this.initialMoney = initialMoney;
        this.users = users;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInitialMoney() {
        return initialMoney;
    }

    public void setInitialMoney(int initialMoney) {
        this.initialMoney = initialMoney;
    }

    public ArrayList<UserGame> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserGame> users) {
        this.users = users;
    }
}
