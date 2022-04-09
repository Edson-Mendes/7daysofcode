package br.com.emendes.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImdbApiClient {

  public String sendGetRequest(String baseUri, String apiKey) {
    try {
      URI uri = new URI(baseUri + apiKey);

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

  private void hasError(String body) throws Exception {
    JsonNode content = new ObjectMapper().readTree(body);
    String errorMessage = content.get("errorMessage").asText();
    if (errorMessage.isEmpty()) {
      return;
    }
    throw new RuntimeException(errorMessage);
  }

}
