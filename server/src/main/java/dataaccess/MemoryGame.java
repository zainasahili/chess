package dataaccess;

import model.GameData;


import java.util.HashSet;

public class MemoryGame implements GameDAO{

    HashSet<GameData> db = new HashSet<>();
    

    @Override
    public HashSet<GameData> getGames() {
        return db;
    }

    @Override
    public void clear() {

    }
}
