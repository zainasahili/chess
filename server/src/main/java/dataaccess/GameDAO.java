package dataaccess;

import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    HashSet<GameData> getGames();
    int create(GameData game) throws DataAccessException;
    boolean exists(int gameID);
    void join(GameData game);
    void clear();
}
