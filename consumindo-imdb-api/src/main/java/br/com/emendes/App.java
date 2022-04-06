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

import br.com.emendes.model.Movie;

public class App {
  public static void main(String[] args) {
    String apiKey = enterApiKey();
    String json = sendRequest(apiKey);

    List<String> moviesList = parseJsonMovies(json);

    List<Movie> movies = parseMovies(moviesList);
    movies.forEach(System.out::println);

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

  private static List<Movie> parseMovies(List<String> moviesList) {
    List<Movie> movies = new ArrayList<Movie>();

    moviesList.forEach(m -> {
      try {
        JsonNode json = new ObjectMapper().readTree(m);
        
        String title = json.get("title").asText();
        String urlImage = json.get("image").asText();
        String rating = json.get("imDbRating").asText();
        String year = json.get("year").asText();

        Movie movie = new Movie(title, urlImage, rating, year);

        movies.add(movie);
      } catch (Exception e) {
        throw new RuntimeException(e.getMessage());
      }
    });

    return movies;
  }

}
