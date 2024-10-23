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
    public GameData getGame(int gameID) {
        for (GameData data: db)
            if (data.gameID() == gameID)
                return data;
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException{

    }


    @Override
    public int create(GameData game) throws DataAccessException{
        if (getGame(game.gameID()) != null)
            throw new DataAccessException("game already created");

        db.add(game);
        return game.gameID();

    }

    @Override
    public void clear() {

    }
}
