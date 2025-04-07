package at.Cinemaland.Service;

import at.Cinemaland.Model.Actor;
import at.Cinemaland.Model.Movie;
import at.Cinemaland.Model.MovieCategory;
import at.Cinemaland.Model.FileData;
import at.Cinemaland.Repository.ActorRepository;
import at.Cinemaland.Repository.MovieRepository;
import at.Cinemaland.Repository.MovieCategoryRepository;
import at.Cinemaland.Repository.FileDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {

    final MovieRepository movieRepository;
    final ActorRepository actorRepository;
    final MovieCategoryRepository movieCategoryRepository;
    final FileDataRepository fileDataRepository;

    public MovieService(MovieRepository movieRepository, ActorRepository actorRepository,
                        MovieCategoryRepository movieCategoryRepository, FileDataRepository fileDataRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.movieCategoryRepository = movieCategoryRepository;
        this.fileDataRepository = fileDataRepository;
    }
    public List<Movie> getMoviesByUser(String username) {
        return movieRepository.findByUser_Username(username);
    }

    public List<Movie> searchMovies(String query) {
        return movieRepository.findByTitleContainingIgnoreCase(query);
    }

    public List<Movie> getMoviesByCategory(String categoryName) {
        return movieRepository.findByCategoryNameIgnoreCase(categoryName);
    }

    public List<Movie> getTopRatedMovies() {
        return movieRepository.findByRatingGreaterThanEqual("8.0");
    }

    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie getMovie(String id) {
        return movieRepository.findById(id).orElse(null);
    }

    public Movie updateMovie(String id, Movie movie) {
        Movie movieToUpdate = movieRepository.findById(id).orElse(null);
        if (movieToUpdate != null) {
            movieToUpdate.setTitle(movie.getTitle());
            movieToUpdate.setDirector(movie.getDirector());
            movieToUpdate.setCast(movie.getCast());
            movieToUpdate.setGenre(movie.getGenre());
            movieToUpdate.setRating(movie.getRating());
            movieToUpdate.setSynopsis(movie.getSynopsis());

            if (movie.getCategory() != null) {
                movieToUpdate.setMovieCategoryId(movie.getCategory().getId());
                MovieCategory category = movieCategoryRepository.findById(movie.getCategory().getId()).orElse(null);
                if (category != null) {
                    movieToUpdate.setCategory(category);
                }
            }

            if (movie.getImage() != null) {
                FileData fileData = fileDataRepository.findById(movie.getImage().getId()).orElse(null);
                if (fileData != null) {
                    movieToUpdate.setImage(fileData);
                }
            }

            return movieRepository.save(movieToUpdate);
        }
        return null;
    }

    public Movie addActors(String movieId, Set<String> actorIds) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie != null) {
            Set<Actor> actors = actorRepository.findAllById(actorIds).stream().collect(Collectors.toSet());
            movie.setActors(actors);
            return movieRepository.save(movie);
        }
        return null;
    }

    public void deleteMovie(String id) {
        movieRepository.deleteById(id);
    }
}