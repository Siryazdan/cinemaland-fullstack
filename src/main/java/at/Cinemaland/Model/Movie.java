package at.Cinemaland.Model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
public class Movie {

    @Id
    private String id;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }

    private String title;
    private String director;
    private String genre;
    private String cast;
    private String rating;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    private int reviewsCount;


    @Column(name = "movie_category_id")
    private String movieCategoryId;

    @ManyToOne
    @JoinColumn(name = "movie_category_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MovieCategory category;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id", referencedColumnName = "rel_id", insertable = false, updatable = false)
    private FileData image;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )

    private Set<Actor> actors;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getMovieCategoryId() {
        return movieCategoryId;
    }

    public void setMovieCategoryId(String movieCategoryId) {
        this.movieCategoryId = movieCategoryId;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public MovieCategory getCategory() {
        return category;
    }

    public void setCategory(MovieCategory category) {
        this.category = category;
    }

    public FileData getImage() {
        return image;
    }

    public void setImage(FileData image) {
        this.image = image;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }
}

