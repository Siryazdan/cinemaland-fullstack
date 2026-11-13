package at.Cinemaland.Service;

import at.Cinemaland.Model.Actor;
import at.Cinemaland.Model.FileData;
import at.Cinemaland.Model.Movie;
import at.Cinemaland.Model.MovieCategory;
import at.Cinemaland.Repository.ActorRepository;
import at.Cinemaland.Repository.FileDataRepository;
import at.Cinemaland.Repository.MovieCategoryRepository;
import at.Cinemaland.Repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MovieService {

    private static final double TOP_RATED_MIN_RATING = 8.0;

    final MovieRepository movieRepository;
    final ActorRepository actorRepository;
    final MovieCategoryRepository movieCategoryRepository;
    final FileDataRepository fileDataRepository;
    final FileDataService fileDataService;

    public MovieService(MovieRepository movieRepository, ActorRepository actorRepository,
                        MovieCategoryRepository movieCategoryRepository, FileDataRepository fileDataRepository,
                        FileDataService fileDataService) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.movieCategoryRepository = movieCategoryRepository;
        this.fileDataRepository = fileDataRepository;
        this.fileDataService = fileDataService;
    }

    public List<Movie> searchMovies(String query) { // Search for movies by title
        return movieRepository.findByTitleContainingIgnoreCase(query == null ? "" : query.trim());
    }

    public List<Movie> getMoviesByCategory(String categoryName) {
        return movieRepository.findByCategoryNameIgnoreCase(categoryName);
    }

    public List<Movie> getTopRatedMovies() { // Get movies with a rating of 8.0 or higher
        return movieRepository.findTopRated(TOP_RATED_MIN_RATING);
    }

    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    public Movie createMovie(Movie movie) {
        movie.setId(null); // a new movie always gets a new ID
        applyCategory(movie, movie);
        return movieRepository.save(movie);
    }

    public Movie getMovie(String id) {
        return movieRepository.findById(id).orElse(null);
    }

    public Movie updateMovie(String id, Movie movie) {
        Movie movieToUpdate = movieRepository.findById(id).orElse(null);
        if (movieToUpdate == null) {
            return null;
        }
        movieToUpdate.setTitle(movie.getTitle());
        movieToUpdate.setDirector(movie.getDirector());
        movieToUpdate.setCast(movie.getCast());
        movieToUpdate.setGenre(movie.getGenre());
        movieToUpdate.setRating(movie.getRating());
        movieToUpdate.setSynopsis(movie.getSynopsis());
        applyCategory(movie, movieToUpdate);

        if (movie.getImage() != null && movie.getImage().getId() != null) {
            FileData fileData = fileDataRepository.findById(movie.getImage().getId()).orElse(null);
            if (fileData != null) {
                movieToUpdate.setImage(fileData);
            }
        }

        return movieRepository.save(movieToUpdate);
    }

    /**
     * The category is saved through the column movie_category_id (the "category" relation is read-only),
     * so the ID must be copied there - otherwise the category is never stored.
     */
    private void applyCategory(Movie source, Movie target) {
        String categoryId = source.getCategory() != null ? source.getCategory().getId() : source.getMovieCategoryId();
        if (categoryId == null) {
            return;
        }
        MovieCategory category = movieCategoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            target.setMovieCategoryId(category.getId());
            target.setCategory(category);
        }
    }

    public Movie addActors(String movieId, Set<String> actorIds) { // Add actors to a movie
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie != null) {
            Set<Actor> actors = movie.getActors() != null ? new HashSet<>(movie.getActors()) : new HashSet<>();
            // add to the existing actors (before, every new actor replaced all others)
            actors.addAll(actorRepository.findAllById(actorIds));
            movie.setActors(actors);
            return movieRepository.save(movie);
        }
        return null;
    }

    public void deleteMovie(String id) {
        fileDataService.deleteFileByRelId(id); // delete the poster too
        movieRepository.deleteById(id);
    }
}
