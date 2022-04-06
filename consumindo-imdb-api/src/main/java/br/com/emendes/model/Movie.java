package br.com.emendes.model;

public class Movie {
  private String title;
  private String urlImage;
  private String rating;
  private String year;

  public Movie(String title, String urlImage, String rating, String year){
    this.title = title;
    this.urlImage = urlImage;
    this.rating = rating;
    this.year = year;
  }

  public String getTitle() {
    return title;
  }

  public String getUrlImage() {
    return urlImage;
  }

  public String getRating() {
    return rating;
  }

  public String getYear() {
    return year;
  }

  @Override
  public String toString() {
    return "{title: "+title+", urlImage: "+urlImage+", rating: "+rating+", year: "+year+"}";
  }
}
