package client;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.GameData;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class ServerFacade {
    private final String url;
    String authToken;
    WebSocket ws;
    String serverDomain;


    public ServerFacade(){
        this("localhost:8080");
    }

    public ServerFacade(String serverDomain){
        this.serverDomain = serverDomain;
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

    public int createGame(String name){
        var body = Map.of("gameName", name);
        var jsonBody = new Gson().toJson(body);
        Map resp = request("POST", "/game", jsonBody);
        if (resp.containsKey("Error")){
            return -1;
        }
        return (int) (double) resp.get("gameID");
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

    public boolean joinGame(ChessGame.TeamColor color, int gameID){
        Map body;
        if (color != null) {
            body = Map.of("playerColor", color, "gameID", gameID);
        }else {
            body = Map.of("gameID", gameID);
        }
        var jsonBody = new Gson().toJson(body);
        var resp = request("PUT", "/game", jsonBody);
        return !resp.containsKey("Error");

    }

    public void connectToWs(){
        try{
            ws = new WebSocket(serverDomain);
        } catch (Exception e) {
            System.out.println("Failed to connect to WebSocket");
        }
    }
    public void joinPlayer(int gameID, ChessGame.TeamColor color) throws IOException {
        UserGameCommand msg = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, color);
        String command = new Gson().toJson(msg);
        ws.sendMessage(command);
    }
    public void observe(int gameID) throws IOException {
        UserGameCommand msg = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, null);
        String command = new Gson().toJson(msg);
        ws.sendMessage(command);
    }
    public void leaveGame(int gameID, ChessGame.TeamColor color) throws IOException {
        UserGameCommand msg = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID, color);
        String command = new Gson().toJson(msg);
        ws.sendMessage(command);
    }
    public void resign(int gameID, ChessGame.TeamColor color) throws IOException {
        UserGameCommand msg = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID, color);
        String command = new Gson().toJson(msg);
        ws.sendMessage(command);
    }

    public void makeMove(int gameID, ChessMove move, ChessGame.TeamColor color) throws IOException {
        UserGameCommand msg = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move, color);
        String command = new Gson().toJson(msg);
        ws.sendMessage(command);
    }

    public void redraw(Integer gameID, ChessGame game) {
        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameID, game);
        String command = new Gson().toJson(msg);
        ws.handleMessage(command);
    }


}
