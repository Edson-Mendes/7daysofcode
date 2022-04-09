package br.com.emendes;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;

import br.com.emendes.client.ImdbApiClient;
import br.com.emendes.model.Movie;
import br.com.emendes.util.ImdbMovieJsonParser;
import br.com.emendes.view.HtmlGenerator;

public class App {

  public static void main(String[] args) throws Exception {

    ImdbApiClient imdbClient = new ImdbApiClient();

    String apiKey = enterApiKey();
    String uri = "https://imdb-api.com/en/API/Top250Movies/";
    String json = imdbClient.sendGetRequest(uri, apiKey);

    List<Movie> movies = new ImdbMovieJsonParser(json).parse();

    String path = "src/main/java/br/com/emendes/view/index.html";
    Writer printWriter = new PrintWriter(path);

    HtmlGenerator html = new HtmlGenerator(printWriter);
    html.generate(movies);

    printWriter.close();
  }

  private static String enterApiKey() {
    Scanner input = new Scanner(System.in);

    // System.out.println("Digite sua api key:");
    // String apiKey = input.nextLine();
    String apiKey = "k_c2u65knb";

    input.close();
    return apiKey;
  }

}
