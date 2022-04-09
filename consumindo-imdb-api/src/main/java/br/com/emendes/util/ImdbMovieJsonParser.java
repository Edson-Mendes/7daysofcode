package br.com.emendes.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.emendes.model.Movie;

public class ImdbMovieJsonParser {

  private String json;

  public ImdbMovieJsonParser(String json) {
    this.json = json;
  }

  public List<Movie> parse() {
    List<String> stringMovies = parseJsonMovies(json);
    return parseMovies(stringMovies);
  }

  private List<String> parseJsonMovies(String source) {
    String[] movies = source
        .replaceAll("],\"errorMessage\":\"\"}", "")
        .replace("{\"items\":[", "")
        .replaceAll("},", "}__")
        .split("__");

    return Arrays.asList(movies);
  }

  private List<Movie> parseMovies(List<String> moviesList) {
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
