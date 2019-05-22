package com.example.appserverpokemon.model;

import java.util.List;

public class Pokemon {

    private int id;
    private String name;
    private Sprites sprites;
    private int price;
    private int idEvo;
    private List<Stats> stats;
    private List<Types> types;
    private List<Moves> moves;



    public Pokemon(int id, String name, Sprites sprites, List<Stats> stats, List<Types> types, List<Moves> moves) {
        this.id = id;
        this.name = name;
        this.sprites = sprites;
        this.stats = stats;
        this.types = types;
        this.moves = moves;
    }
    public Pokemon(){}

    public List<Moves> getMoves() {
        return moves;
    }

    public void setMoves(List<Moves> moves) {
        this.moves = moves;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIdEvo() {
        return idEvo;
    }

    public void setIdEvo(int idEvo) {
        this.idEvo = idEvo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public void setStats(List<Stats> stats) {
        this.stats = stats;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public List<Stats> getStats() {
        return stats;
    }

    public List<Types> getTypes() {
        return types;
    }

    public void setTypes(List<Types> types) {
        this.types = types;
    }
}


