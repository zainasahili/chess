package client;

import com.google.gson.Gson;
import model.GameData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class ServerFacade {
    private final String url;

    public ServerFacade(String serverDomain) {
       url = "http://" + serverDomain;
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
        return !request("POST", "/user", jsonBody).containsKey("Error");
    }

    public boolean login(String username, String password){
        var body = Map.of("username", username, "password", password);
        var jsonBody = new Gson().toJson(body);
        return !request("POST", "/session", jsonBody).containsKey("Error");
    }

    public void logout(){
        request("DELETE", "/session", null);
    }

    public boolean createGame(String name){
        var body = Map.of("name", name);
        var jsonBody = new Gson().toJson(body);
        return !request("POST", "/game", jsonBody).containsKey("Error");
    }

    public Collection<GameData> listGame(){
        var resp = request("GET","/game", null);
        if (!resp.containsKey("Error")){
            return HashSet.newHashSet(16);
        }
        return new Gson().fromJson((Reader) resp, Collection.class);
    }

    public void joinGame(){

    }

    public void observeGame(){}

}
