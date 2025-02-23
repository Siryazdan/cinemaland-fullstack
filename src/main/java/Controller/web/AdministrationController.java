package Controller.web;

import Model.Movie;
import Service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/administration")
public class AdministrationController {

    private final MovieService movieService;

    public AdministrationController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping()
    public String administration(Model model) {

        List<Movie> movies = movieService.getMovies();
        model.addAttribute("movies", movies);

        return "administration/administration";
    }

    @GetMapping("/movie/create")
    public String createMovie(Model model) {

        model.addAttribute("movie", new Movie());
        return "administration/movie/create";
    }
}
