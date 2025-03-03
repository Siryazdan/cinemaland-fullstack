package at.Cinemaland.Controller.web;

import at.Cinemaland.Model.Movie;
import at.Cinemaland.Model.MovieCategory;
import at.Cinemaland.Service.MovieCategoryService;
import at.Cinemaland.Service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
public class MovieController {

    final MovieService movieService;
    final MovieCategoryService movieCategoryService;

    public MovieController(MovieService movieService, MovieCategoryService movieCategoryService) {
        this.movieService = movieService;
        this.movieCategoryService = movieCategoryService;
    }

    @GetMapping("/")
    public String index(Model model) {

        List<Movie> movies = this.movieService.getMovies();
        model.addAttribute("movies", movies);

        List<MovieCategory> categories = this.movieCategoryService.getCategories();
        model.addAttribute("categories", categories);

        return "index";
    }
}
