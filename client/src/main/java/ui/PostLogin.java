package ui;

import client.ServerFacade;
import model.GameData;

import java.util.Collection;
import java.util.HashSet;

public class PostLogin {

    ServerFacade server;

    public PostLogin(ServerFacade server) {
        this.server = server;
    }

    public String help(){
        return null;
    }

    public void logout(){}

    public boolean createGame(){
        return true;
    }

    public Collection<GameData> listGames(){
        return HashSet.newHashSet(5);
    }

    public void playGame(){

    }

    public void observeGame(){}

}
