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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("")
public class MovieController {

    final MovieService movieService;
    final MovieCategoryService movieCategoryService;

    // Constructor to inject services
    public MovieController(MovieService movieService,
                           MovieCategoryService movieCategoryService) {
        this.movieService = movieService;
        this.movieCategoryService = movieCategoryService;
    }

    @GetMapping("/")
    public String index(Model model) {

        List<Movie> movies = movieService.getMovies();
        List<MovieCategory> categories = movieCategoryService.getCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("movies", movies);

        return "index";
    }

    @GetMapping("/movie/{id}") // URL to access the movie details
    public String getMovieDetails(@PathVariable("id") String id, Model model) {
        Movie movie = movieService.getMovie(id);
        if (movie != null) {
            // Dynamischen YouTube-Trailer-Link generieren
            String youtubeTrailerLink = "https://www.youtube.com/results?search_query=" + movie.getTitle() + " trailer";

            model.addAttribute("movie", movie);
            model.addAttribute("youtubeTrailerLink", youtubeTrailerLink);

            return "movie-details";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/category/{categoryName}") // URL to access the movies by category
    public String getMoviesByCategory(@PathVariable("categoryName") String categoryName, Model model) {
        List<Movie> movies = movieService.getMoviesByCategory(categoryName);
        model.addAttribute("movies", movies);
        model.addAttribute("categoryName", categoryName);
        return "category-details";
    }

    @GetMapping("/search") // URL to access the search functionality
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

    @GetMapping("/top-rated") // URL to access the top-rated movies
    public String getTopRatedMovies(Model model) {
        List<Movie> topRatedMovies = movieService.getTopRatedMovies();
        model.addAttribute("movies", topRatedMovies);
        return "top-rated-movies";
    }
}