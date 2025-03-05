package at.cinemaland.Service;


import at.cinemaland.Model.Actor;
import at.cinemaland.Model.Movie;
import at.cinemaland.Repository.ActorRepository;
import at.cinemaland.Repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.boot.context.properties.bind.Bindable.setOf;

@Service
public class MovieService {

    final MovieRepository movieRepository;
    final ActorRepository actorRepository;

    public MovieService(MovieRepository movieRepository, ActorRepository actorRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
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
            movieToUpdate.setGenre(movie.getGenre());
            movieToUpdate.setDirector(movie.getDirector());
            movieToUpdate.setSynopsis(movie.getSynopsis());


            Movie entity = movieRepository.save(movieToUpdate);

            //TODO: update category as well with entity.getId()
            //TODO: update fileData as well with entity.getId()

            return entity;
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










