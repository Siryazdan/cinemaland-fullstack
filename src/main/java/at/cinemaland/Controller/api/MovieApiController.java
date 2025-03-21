package at.cinemaland.Controller.api;

import at.cinemaland.Model.Movie;
import at.cinemaland.Service.MovieService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/movies/")
public class MovieApiController {

    final MovieService movieService;

    public MovieApiController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }

    @GetMapping
    public Iterable<Movie> getMovies() {
        return movieService.getMovies();
    }

    @GetMapping("{id}")
    public Movie getMovie(@PathVariable String id) {
        return movieService.getMovie(id);
    }

    @PutMapping("{id}")
    public Movie updateMovie(@RequestParam String id, @RequestBody Movie movie) {
        return movieService.updateMovie(id, movie);
    }

    @DeleteMapping("{id}")
    public void deleteMovie(@PathVariable String id) {
        movieService.deleteMovie(id);
    }
}
