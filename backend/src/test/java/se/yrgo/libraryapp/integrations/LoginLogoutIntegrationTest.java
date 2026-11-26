package se.yrgo.libraryapp.integrations;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpPost;
import io.jooby.JoobyTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import se.yrgo.libraryapp.App;
import se.yrgo.libraryapp.entities.forms.LoginData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("integration")
@Testcontainers
@JoobyTest(value = App.class, factoryMethod = "createApp")
public class LoginLogoutIntegrationTest {
    @Container
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>
            ("mysql:8.0.29");
    @Test
    public void alwaysOkToLogout(int serverPort) throws IOException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder(URI.create("http://localhost:" +
                    serverPort + "/logout")).build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            assertEquals("", resp.body());
            assertEquals(HttpURLConnection.HTTP_OK, resp.statusCode());
        }
        catch (IllegalArgumentException|InterruptedException ex) {
            fail(ex);
        }
    }

    @Test
    public void loginLogoutTest(int serverPort) throws IOException, InterruptedException {
        String json = "{\"username\":\"test\",\"password\":\"yrgoP4ssword\"}";
        String uri = "http://localhost:" + serverPort;

        try  {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder(URI.create(uri + "/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> loginResp = client.send(req, HttpResponse.BodyHandlers.ofString());
            req = HttpRequest.newBuilder(URI.create(uri + "/logout")).build();
            HttpResponse<String> logoutResp = client.send(req, HttpResponse.BodyHandlers.ofString());
            assertEquals(HttpURLConnection.HTTP_OK, loginResp.statusCode());
            assertEquals(HttpURLConnection.HTTP_OK, logoutResp.statusCode());
        }
        catch (IllegalArgumentException|InterruptedException ex) {
        fail(ex);
        }
    }

    @Test
    public void incorrectLoginTest(int serverPort) throws IOException{
        String json = "{\"username\":\"wrongUser\",\"password\":\"yrgoP4ssword\"}";
        String uri = "http://localhost:" + serverPort + "/login";
        try  {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, resp.statusCode());
        }
        catch (IllegalArgumentException|InterruptedException ex) {
            fail(ex);
        }
    }

    @Test
    public void registerUser(int serverPort) throws IOException{
        String json = "{\"name\":\"newUser\",\"realName\":\"David\", \"password\":\"yrgoP4ssword\"}";
        String uri = "http://localhost:" + serverPort + "/register";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            assertEquals(HttpURLConnection.HTTP_OK, resp.statusCode());
        }
        catch (IllegalArgumentException|InterruptedException ex) {
            fail(ex);
        }
    }

    @Test
    public void checkAvailableName(int serverPort) throws IOException{
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder(URI.create("http://localhost:" +
                    serverPort + "/register/available?name=goodUser")).build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            assertEquals(HttpURLConnection.HTTP_OK, resp.statusCode());
        }
        catch (IllegalArgumentException|InterruptedException ex) {
            fail(ex);
        }

    }

    public static App createApp() {
        System.setProperty("db.url", mySQLContainer.getJdbcUrl());
        System.setProperty("db.user", mySQLContainer.getUsername());
        System.setProperty("db.password", mySQLContainer.getPassword());
        return new App();
    }

    @AfterAll
    static void reset() {
        System.clearProperty("db.url");
        System.clearProperty("db.user");
        System.clearProperty("db.password");
    }
}