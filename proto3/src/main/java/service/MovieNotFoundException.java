package service;

public class MovieNotFoundException extends Exception {
  private String name;

  public MovieNotFoundException(String movieName) {
    this.name = movieName;
  }
}
