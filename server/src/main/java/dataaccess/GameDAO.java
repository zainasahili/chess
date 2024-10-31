package dataaccess;

import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    HashSet<GameData> getGames() throws DataAccessException;
    int create(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    void clear();
}
