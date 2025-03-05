package at.Cinemaland.Model;


import jakarta.persistence.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
public class Movie {

    @Id
    private String id;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }

    private String Title;
    private String director;
    private String cast;
    private String genre;
    private String rating;
    private String synopsis;
    private int reviewsCount;



    @ManyToOne
    @JoinColumn(name = "movie_category_id", referencedColumnName = "id")
    private MovieCategory category;

    public MovieCategory getCategory() {
        return category;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public void setCategory(MovieCategory category) {
        this.category = category;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id", referencedColumnName = "rel_id", insertable = false, updatable = false)
    private FileData image;

    public FileData getImage() {
        return image;
    }

    public void setImage(FileData image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
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

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

}

