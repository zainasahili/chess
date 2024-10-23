package dataaccess;

import model.GameData;


import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class MemoryGame implements GameDAO{

    HashSet<GameData> db = new HashSet<>();


    @Override
    public HashSet<GameData> getGames() {
        return db;
    }


    @Override
    public boolean exists(int gameID) {
        for (GameData data: db)
            if (data.gameID() == gameID)
                return true;
        return false;
    }

    @Override
    public void join(GameData game) {

    }


    @Override
    public int create(GameData game) throws DataAccessException{
        if (exists(game.gameID()))
            throw new DataAccessException("game alerady created");

        db.add(game);
        return game.gameID();

    }

    @Override
    public void clear() {

    }
}
