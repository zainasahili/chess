package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    void setup(){
        server.clear();
        facade = new ServerFacade("localhost:" + port);

    }

    @AfterEach
    void after(){
        server.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void validRegister() {
        Assertions.assertTrue(facade.register("username", "password", "email"));
    }

    @Test
    public void invalidRegister(){
        facade.register("username", "password", "email");
        Assertions.assertFalse(facade.register("username", "password", "email"));
    }

    @Test
    public void validLogin(){
        facade.register("username", "password", "email");
        Assertions.assertTrue(facade.login("username", "password"));
    }

    @Test
    public void invalidLogin(){
        Assertions.assertFalse(facade.login("username", "password"));
    }

    @Test
    public void validLogout(){
        facade.register("username", "password", "email");
        facade.login("username", "password");
        Assertions.assertTrue(facade.logout());
    }

    @Test
    public void invalidLogout(){
        Assertions.assertFalse(facade.logout());
    }

    @Test
    public void validCreate(){
        facade.register("username", "password", "email");
        facade.login("username", "password");
        Assertions.assertTrue(facade.createGame("BYU"));
    }

    @Test
    public void invalidCreate(){
        Assertions.assertFalse(facade.createGame("BYU"));
    }

}
