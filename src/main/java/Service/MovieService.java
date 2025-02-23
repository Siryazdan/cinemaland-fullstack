package Service;

import Model.Movie;
import Repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
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

        // Movie entity = movieRepository.findById(id).orElse(null);
        //if (entity != null) {
        //  movie.setId(id);
        //movie.setActors(entity.getActors());

        return movieRepository.save(movie);
    }

    public void deleteMovie(String id) {
        movieRepository.deleteById(id);
    }

}










