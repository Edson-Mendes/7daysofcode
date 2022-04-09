package br.com.emendes.view;

import java.io.Writer;
import java.util.List;

import br.com.emendes.model.Movie;

public class HtmlGenerator {

  private Writer printWriter;

  public HtmlGenerator() {
  }

  public HtmlGenerator(Writer printWriter) {
    this.printWriter = printWriter;
  }

  public void generate(List<Movie> movies) {
    try {
      printWriter.write(html(movies));
    } catch (Exception e) {
      throw new RuntimeException("Não foi possível escrever no arquivo");
    }
  }

  // TODO: Adicionar header, footer e centralizar o conteúdo.
  private String head() {
    String head = """

        <head>
          <meta charset=\"UTF-8\">
          <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">
          <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">
          <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css\"
              rel=\"stylesheet\"
              integrity=\"sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC\"
              crossorigin=\"anonymous\">
          <title>Top 250 Movies</title>
        </head>""";

    return head;
  }

  private String body(List<Movie> movies) {
    String cards = "";

    for (Movie movie : movies) {
      cards = cards.concat(card(movie));
    }

    String body = """

        <body>
          <div class=\".container p-6\">
            <div class=\"row align-items-center\">
          %s
            </div>
          </div>
        </body>""";
    return String.format(body, cards);
  }

  private String html(List<Movie> movies) {
    String html = """
        <!DOCTYPE html>
        <html lang=\"pt-BR\">
          %s
          %s
        </html>""";

    return String.format(html, head(), body(movies));
  }

  private String card(Movie movie) {
    String title = movie.getTitle();
    String rating = movie.getRating();
    String urlImage = movie.getUrlImage();
    String year = movie.getYear();

    String card = """
        <div class=\"card text-white bg-dark mb-3 col-3\" style=\"max-width: 18rem;\">
          <h4 class=\"card-header\">%s</h4>
          <div class=\"card-body\">
            <img class=\"card-img\" src=\"%s\" alt=\"%s\">
            <p class=\"card-text mt-2\">Nota: %s - Ano: %s</p>
          </div>
        </div>
        """;

    return String.format(card, title, urlImage, title, rating, year);
  }

  private String header() {
    // TODO: Em desenvolvimento
    return "";
  }

  private String footer() {
    // TODO: Em desenvolvimento
    return "";
  }

}
