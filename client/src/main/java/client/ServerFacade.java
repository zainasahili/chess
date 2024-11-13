package client;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.GameData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class ServerFacade {
    private final String url;
    String authToken;

    public ServerFacade(String serverDomain) {
       url = "http://" + serverDomain;
    }
    private String getAuthToken(){
        return authToken;
    }
    private void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    private Map request(String method, String endpoint, String body){
        Map reqmap;
      try{
          HttpURLConnection http = connection(method, endpoint, body);

          try (InputStream respBody = http.getInputStream()) {
              InputStreamReader inputStreamReader = new InputStreamReader(respBody);
              reqmap = new Gson().fromJson(inputStreamReader, Map.class);
          }
      } catch (IOException | URISyntaxException e){
          return Map.of("Error", e.getMessage());
      }
      return reqmap;
    }
    private HttpURLConnection connection(String method, String endpoint, String body ) throws IOException, URISyntaxException {
        URI uri = new URI(url + endpoint);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);

        if (getAuthToken() != null){
            http.addRequestProperty("authorization", getAuthToken());
        }

        if (!Objects.equals(body, null)){
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
        http.connect();
        return http;
    }
    public boolean register(String username, String password, String email){
        var body = Map.of("username", username, "password", password, "email", email);
        var jsonBody = new Gson().toJson(body);
        Map response = request("POST", "/user", jsonBody);
        if (response.containsKey("Error")){
            return false;
        }

        setAuthToken((String) response.get("authToken"));
        return true;
    }

    public boolean login(String username, String password){
        var body = Map.of("username", username, "password", password);
        var jsonBody = new Gson().toJson(body);
        Map response = request("POST", "/session", jsonBody);
        if (response.containsKey("Error")){
            return false;
        }

        setAuthToken((String) response.get("authToken"));
        return true;
    }

    public boolean logout(){
        Map response = request("DELETE", "/session", null);
        if (response.containsKey("Error")){
            return false;
        }
        setAuthToken(null);
        return true;
    }

    public boolean createGame(String name){
        var body = Map.of("gameName", name);
        var jsonBody = new Gson().toJson(body);
        return !request("POST", "/game", jsonBody).containsKey("Error");
    }

    public Collection<GameData> listGames(){
        var resp = request("GET","/game", null);
        if (resp.containsKey("Error")){
            return new HashSet<>();
        }
        Object gamesObj = resp.get("games");
        String gamesJson = new Gson().toJson(gamesObj);

        return new Gson().fromJson(gamesJson, new TypeToken<Collection<GameData>>(){}.getType());
    }

    public boolean joinGame(String color, int gameID){
        var body = Map.of("playerColor", color, "gameID", gameID);
        var jsonBody = new Gson().toJson(body);
        var resp = request("PUT", "/game", jsonBody);
        return !resp.containsKey("Error");

    }

    public void observeGame(){}

}
