package client;

import model.GameData;

import java.util.Collection;
import java.util.HashSet;

public class ServerFacade {
    private final String url;

    public ServerFacade(String url) {
        this.url = url;
    }



    public boolean register(){
        return true;
    }

    public boolean login(){
        return true;
    }

    public void logout(){

    }

    public boolean createGame(String name){
        return true;
    }

    public Collection<GameData> listGame(){
        return HashSet.newHashSet(5);
    }

    public void joinGame(){

    }

    public void observeGame(){}

}
