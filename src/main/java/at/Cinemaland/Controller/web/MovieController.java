package at.Cinemaland.Controller.web;


import at.Cinemaland.Model.Movie;
import at.Cinemaland.Model.MovieCategory;
import at.Cinemaland.Service.MovieCategoryService;
import at.Cinemaland.Service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("")
public class MovieController {

    final MovieService movieService;
    final MovieCategoryService movieCategoryService;

    public MovieController(MovieService movieService,
                           MovieCategoryService movieCategoryService) {
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
        model.addAttribute("movies", movies);
        return "index";
    }
}
