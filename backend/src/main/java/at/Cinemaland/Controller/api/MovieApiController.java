package at.Cinemaland.Controller.api;

import at.Cinemaland.Model.Actor;
import at.Cinemaland.Model.Movie;
import at.Cinemaland.Service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("/api/movies/")
public class MovieApiController {

    final MovieService movieService;

    public MovieApiController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }

    @GetMapping
    public Iterable<Movie> getMovies() {
        return movieService.getMovies();
    }

    @GetMapping("{id}")
    public Movie getMovie(@PathVariable("id") String id) {
        return findOr404(id);
    }

    @GetMapping("{id}/actors/")
    public Set<Actor> getMovieActors(@PathVariable("id") String id) {
        return findOr404(id).getActors();
    }

    @PutMapping("{id}")
    public Movie updateMovie(@PathVariable("id") String id, @RequestBody Movie movie) {
        Movie updated = movieService.updateMovie(id, movie);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
        return updated;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable("id") String id) {
        findOr404(id);
        movieService.deleteMovie(id);
    }

    private Movie findOr404(String id) {
        Movie movie = movieService.getMovie(id);
        if (movie == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
        return movie;
    }
}
