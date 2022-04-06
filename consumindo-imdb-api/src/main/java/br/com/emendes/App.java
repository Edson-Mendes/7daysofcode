package br.com.emendes;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
  public static void main(String[] args) {
    String apiKey = enterApiKey();
    String json = sendRequest(apiKey);

    List<String> movies = parseJsonMovies(json);

    List<String> titles = parseField(movies, "title");
    titles.forEach(System.out::println);

    List<String> images = parseField(movies, "image");
    images.forEach(System.out::println);

  }

  private static String enterApiKey() {
    Scanner input = new Scanner(System.in);

    System.out.println("Digite sua api key:");
    String apiKey = input.nextLine();

    input.close();
    return apiKey;
  }

  private static String sendRequest(String apiKey) {
    try {
      URI uri = new URI("https://imdb-api.com/pt/API/Top250Movies/" + apiKey);

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      String body = response.body();

      hasError(body);
      return body;
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private static void hasError(String body) throws Exception {
    JsonNode content = new ObjectMapper().readTree(body);
    String errorMessage = content.get("errorMessage").asText();
    if (errorMessage.isEmpty()) {
      return;
    }
    throw new RuntimeException(errorMessage);
  }

  private static List<String> parseJsonMovies(String source) {
    String[] movies = source
        .replaceAll("],\"errorMessage\":\"\"}", "")
        .replace("{\"items\":[", "")
        .replaceAll("},", "}__")
        .split("__");

    return Arrays.asList(movies);
  }

  private static List<String> parseField(List<String> movies, String field) {
    List<String> fieldList = new ArrayList<>();

    movies.forEach(movie -> {
      try {
        JsonNode json = new ObjectMapper().readTree(movie);
        String title = json.get(field).asText();
        fieldList.add(title);
      } catch (Exception e) {
        throw new RuntimeException(e.getMessage());
      }
    });

    return fieldList;
  }

}
