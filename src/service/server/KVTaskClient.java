package service.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    String API_TOKEN;
    HttpRequest.Builder builder = HttpRequest.newBuilder();
    HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(String url) throws IOException, InterruptedException {
        URI uri = URI.create(url);
        HttpRequest requestRegister = builder
                .GET()
                .uri(URI.create(uri + "/register"))
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(requestRegister, handler);
        API_TOKEN = response.body();
        System.out.println("Клиент зарегистрирован, API key: " + response.body());
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest requestSave = builder
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(requestSave, handler);
    }

    public String load(String key) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest requestSave = builder
                .GET()
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(requestSave, handler);
        return response.body();
    }
}