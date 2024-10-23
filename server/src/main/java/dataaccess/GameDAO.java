package dataaccess;

import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    HashSet<GameData> getGames();
    void clear();
}
