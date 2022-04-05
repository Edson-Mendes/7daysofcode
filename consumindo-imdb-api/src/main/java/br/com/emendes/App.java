package br.com.emendes;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
  public static void main(String[] args) {
    String apiKey = enterApiKey();
    String json = sendRequest(apiKey);

    parseJsonTitles(json).forEach(System.out::println);
    parseJsonImages(json).forEach(System.out::println);

  }

  private static List<String> parseJsonTitles(String source) {
    try {
      JsonNode content = new ObjectMapper().readTree(source);
      Iterator<JsonNode> iterator = content.get("items").iterator();

      List<String> titles = new ArrayList<>();

      iterator.forEachRemaining(jsonNode -> {
        titles.add(jsonNode.get("title").asText().replaceAll("\"", ""));
      });

      return titles;
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private static List<String> parseJsonImages(String source) {
    try {
      JsonNode content = new ObjectMapper().readTree(source);
      Iterator<JsonNode> iterator = content.get("items").iterator();

      List<String> images = new ArrayList<>();

      iterator.forEachRemaining(jsonNode -> {
        images.add(jsonNode.get("image").asText().replaceAll("\"", ""));
      });

      return images;
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
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

}
