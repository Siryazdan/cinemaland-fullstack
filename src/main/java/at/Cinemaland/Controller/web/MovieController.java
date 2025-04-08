package at.Cinemaland.Controller.web;

import at.Cinemaland.Model.Movie;
import at.Cinemaland.Model.MovieCategory;
import at.Cinemaland.Service.MovieCategoryService;
import at.Cinemaland.Service.MovieService;
import at.Cinemaland.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("")
public class MovieController {

    final MovieService movieService;
    final MovieCategoryService movieCategoryService;
    final UserService userService; // Inject UserService

    // Constructor to inject services
    public MovieController(MovieService movieService,
                           MovieCategoryService movieCategoryService,
                           UserService userService) {
        this.movieService = movieService;
        this.movieCategoryService = movieCategoryService;
        this.userService = userService;  // Initialize UserService
    }

    @GetMapping("/")
    public String index(Model model) {

        List<Movie> movies = movieService.getMovies();
        List<MovieCategory> categories = movieCategoryService.getCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("movies", movies);

        return "index";
    }

    @GetMapping("/movie/{id}")
    public String getMovieDetails(@PathVariable("id") String id, Model model) {
        Movie movie = movieService.getMovie(id);
        if (movie != null) {
            model.addAttribute("movie", movie);
            return "movie-details";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/category/{categoryName}")
    public String getMoviesByCategory(@PathVariable("categoryName") String categoryName, Model model) {
        List<Movie> movies = movieService.getMoviesByCategory(categoryName);
        model.addAttribute("movies", movies);
        model.addAttribute("categoryName", categoryName);
        return "category-details";
    }

    @GetMapping("/search")
    public String searchMovies(@RequestParam("query") String query, Model model) {
        List<Movie> movies = movieService.searchMovies(query);

        if (movies.isEmpty()) {
            model.addAttribute("message", "No movies found for your search.");
        } else {
            model.addAttribute("message", "Search results for: " + query);
        }

        model.addAttribute("movies", new ArrayList<>(movies));
        return "index";
    }

    @GetMapping("/top-rated")
    public String getTopRatedMovies(Model model) {
        List<Movie> topRatedMovies = movieService.getTopRatedMovies();
        model.addAttribute("movies", topRatedMovies);
        return "top-rated-movies";
    }

}