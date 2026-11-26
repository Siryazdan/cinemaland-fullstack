package at.Cinemaland.Service;

import at.Cinemaland.Model.Actor;
import at.Cinemaland.Model.FileData;
import at.Cinemaland.Model.Movie;
import at.Cinemaland.Model.MovieCategory;
import at.Cinemaland.Repository.ActorRepository;
import at.Cinemaland.Repository.FileDataRepository;
import at.Cinemaland.Repository.MovieCategoryRepository;
import at.Cinemaland.Repository.MovieRepository;
import at.Cinemaland.Security.CurrentUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Movies of the logged-in user. Every method only reads or changes the current user's own movies -
 * a movie of another user behaves as if it didn't exist (null / nothing happens).
 */
@Service
public class MovieService {

    private static final double TOP_RATED_MIN_RATING = 8.0;

    final MovieRepository movieRepository;
    final ActorRepository actorRepository;
    final MovieCategoryRepository movieCategoryRepository;
    final FileDataRepository fileDataRepository;
    final FileDataService fileDataService;
    final CurrentUser currentUser;

    public MovieService(MovieRepository movieRepository, ActorRepository actorRepository,
                        MovieCategoryRepository movieCategoryRepository, FileDataRepository fileDataRepository,
                        FileDataService fileDataService, CurrentUser currentUser) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.movieCategoryRepository = movieCategoryRepository;
        this.fileDataRepository = fileDataRepository;
        this.fileDataService = fileDataService;
        this.currentUser = currentUser;
    }

    public List<Movie> searchMovies(String query) { // Search for movies by title
        return movieRepository.findByUserIdAndTitleContainingIgnoreCase(currentUser.id(), query == null ? "" : query.trim());
    }

    public List<Movie> getMoviesByCategory(String categoryName) {
        return movieRepository.findByUserIdAndCategoryNameIgnoreCase(currentUser.id(), categoryName);
    }

    public List<Movie> getTopRatedMovies() { // Get movies with a rating of 8.0 or higher
        return movieRepository.findTopRated(currentUser.id(), TOP_RATED_MIN_RATING);
    }

    public List<Movie> getMovies() {
        return movieRepository.findByUserId(currentUser.id());
    }

    public Movie createMovie(Movie movie) {
        movie.setId(null); // a new movie always gets a new ID
        movie.setUserId(currentUser.id()); // always the logged-in user, never a value sent by the client
        movie.setActors(new HashSet<>()); // actors are added separately, and only the user's own
        applyCategory(movie, movie);
        return movieRepository.save(movie);
    }

    /** The movie, or null if it doesn't exist or belongs to another user. */
    public Movie getMovie(String id) {
        return movieRepository.findByIdAndUserId(id, currentUser.id()).orElse(null);
    }

    public Movie updateMovie(String id, Movie movie) {
        Movie movieToUpdate = getMovie(id);
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
            // only an image that already belongs to this movie
            if (fileData != null && id.equals(fileData.getRelId())) {
                movieToUpdate.setImage(fileData);
            }
        }
        return movieRepository.save(movieToUpdate);
    }

    /**
     * The category is saved through the column movie_category_id (the "category" relation is read-only),
     * so the ID must be copied there - otherwise the category is never stored.
     * Only the user's own categories can be used.
     */
    private void applyCategory(Movie source, Movie target) {
        String categoryId = source.getCategory() != null ? source.getCategory().getId() : source.getMovieCategoryId();
        if (categoryId == null) {
            return;
        }
        MovieCategory category = movieCategoryRepository.findByIdAndUserId(categoryId, currentUser.id()).orElse(null);
        // a category of another user (or a wrong ID) is never stored
        target.setMovieCategoryId(category != null ? category.getId() : null);
        target.setCategory(category);
    }

    public Movie addActors(String movieId, Set<String> actorIds) { // Add actors to a movie
        Movie movie = getMovie(movieId);
        if (movie != null) {
            Set<Actor> actors = movie.getActors() != null ? new HashSet<>(movie.getActors()) : new HashSet<>();
            // add to the existing actors - only the user's own actors
            actors.addAll(actorRepository.findByIdInAndUserId(actorIds, currentUser.id()));
            movie.setActors(actors);
            return movieRepository.save(movie);
        }
        return null;
    }

    /** Deletes the movie if it belongs to the current user. Returns false if not found. */
    public boolean deleteMovie(String id) {
        Movie movie = getMovie(id);
        if (movie == null) {
            return false;
        }
        fileDataService.deleteFileByRelId(id); // delete the poster too
        movieRepository.delete(movie);
        return true;
    }
}
