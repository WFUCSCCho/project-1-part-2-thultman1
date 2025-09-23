/****************************************************
 * @file: Movie.java
 * @description: Represents a Movie record parsed from CSV. Comparable by alphabetical name so Movies can be stored in a BST keyed by name.
 * @author: Tim Hultman
 * @date: 9/23/25
 ****************************************************/
public class Movie implements Comparable<Movie> {
    private String name;
    private int year;
    private String duration;
    private String genre;
    private double rating;
    private String description;
    private String director;
    private String stars;

    /**
     * Default constructor
     * Parameter none
     * Return void
     */
    public Movie() {}

    /**
     * Parameter constructor
     * Parameter String name, int year, String duration, String genre, double rating, String description, String director, String stars
     * Return void
     */
    public Movie(String name, int year, String duration, String genre, double rating, String description, String director, String stars) {
        this.name = name;
        this.year = year;
        this.duration = duration;
        this.genre = genre;
        this.rating = rating;
        this.description = description;
        this.director = director;
        this.stars = stars;
    }

    /**
     * Getters for all fields
     * Parameter none
     * Return String name, int year, String duration, String genre, double rating, String description, String director, String stars
     */
    public String getName() {
        return name; }
    public int getYear() {
        return year; }
    public String getDuration() {
        return duration; }
    public String getGenre() {
        return genre; }
    public double getRating() {
        return rating; }
    public String getDescription() {
        return description; }
    public String getDirector() {
        return director; }
    public String getStars() {
        return stars; }

    /**
     * Setters for all fields
     * Parameter String name, int year, String duration, String genre, double rating, String description, String director, String stars
     * Return void
     */
    public void setName(String name) {
        this.name = name; }
    public void setYear(int year) {
        this.year = year; }
    public void setDuration(String duration) {
        this.duration = duration; }
    public void setGenre(String genre) {
        this.genre = genre; }
    public void setRating(double rating) {
        this.rating = rating; }
    public void setDescription(String description) {
        this.description = description; }
    public void setDirector(String director) {
        this.director = director; }
    public void setStars(String stars) {
        this.stars = stars; }

    /**
     * toString Return key details as formatted string
     * Parameter none
     * Return String
     */
    @Override
    public String toString() {
        return name + " (" + year + "), " + duration + " - " + genre + " - " + rating
                + " | " + description + " | Directed by: " + director + " | Stars: " + stars;
    }

    /**
     * equals Compare by movie name (case-insensitive)
     * Parameter Object obj
     * Return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Movie)) {
            return false;
        }
        Movie other = (Movie) obj;
        return this.name != null && this.name.equalsIgnoreCase(other.name);
    }

    /**
     * compareTo Alphabetical by name (case-insensitive)
     * Parameter Movie other
     * Return int
     */
    @Override
    public int compareTo(Movie other) {
        if (this.name == null && other.name == null) {
            return 0;
        }
        if (this.name ==null) {
            return -1;
        }
        if (other.name == null) {
            return 1;
        }
        return this.name.compareToIgnoreCase(other.name);
    }
}
