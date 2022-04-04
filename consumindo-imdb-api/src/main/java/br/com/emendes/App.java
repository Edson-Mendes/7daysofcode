package br.com.emendes;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Scanner;

public class App {

  public static void main(String[] args) throws Exception {
    Scanner input = new Scanner(System.in);

    System.out.println("Digite sua api key:");
    String apiKey = input.nextLine();

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest
        .newBuilder()
        .uri(new URI("https://imdb-api.com/pt/API/Top250Movies/"+apiKey))
        .GET()
        .build();

    client.sendAsync(request, BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(s -> {
          System.out.println(s);
        })
        .join();

    input.close();
  }
}

