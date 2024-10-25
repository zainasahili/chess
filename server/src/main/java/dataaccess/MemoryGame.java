package dataaccess;

import model.GameData;


import java.util.HashSet;


public class MemoryGame implements GameDAO{

    HashSet<GameData> db;

    public MemoryGame() {
        db = HashSet.newHashSet(16);
    }


    @Override
    public HashSet<GameData> getGames() {
        return db;
    }


    @Override
    public GameData getGame(int gameID) {
        for (GameData data: db) {
            if (data.gameID() == gameID) {
                return data;
            }
        }
        return null;
    }

    @Override
    public void updateGame(GameData game){
        db.remove(getGame(game.gameID()));
        db.add(game);
    }


    @Override
    public int create(GameData game) throws DataAccessException{
        if (getGame(game.gameID()) != null) {
            throw new DataAccessException("game already created");
        }

        db.add(game);
        return game.gameID();

    }

    @Override
    public void clear() {
        db = new HashSet<>();
    }
}
