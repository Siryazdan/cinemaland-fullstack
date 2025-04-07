package at.Cinemaland.Controller.web;

import at.Cinemaland.Model.Movie;
import at.Cinemaland.Model.User;
import at.Cinemaland.Service.MovieCategoryService;
import at.Cinemaland.Service.MovieService;
import at.Cinemaland.Service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Movie> movies;
        if ("Sirnumb".equals(username)) {
            movies = movieService.getMovies(); // Admin sees all
        } else {
            movies = movieService.getMoviesByUser(username); // Regular users see only theirs
        }

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

    @GetMapping("/dashboard")
    public String userDashboard(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // If the user is admin, redirect to the home page (or you can show a special admin dashboard)
        if ("Sirnumb".equals(username)) {
            return "redirect:/";  // or show a special page for admin
        }

        List<Movie> userMovies = movieService.getMoviesByUser(username); // Get movies specific to the user
        model.addAttribute("movies", userMovies);
        return "dashboard";
    }

    @PostMapping("/dashboard/movie/create")
    public String createMovie(@RequestParam String title,
                              @RequestParam String director,
                              @RequestParam String rating,
                              @RequestParam String synopsis) {
        // Get the logged-in username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find the current user
        User user = userService.findByUsername(username);

        // Create a new movie object
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDirector(director);
        movie.setRating(rating);
        movie.setSynopsis(synopsis);
        movie.setUser(user); // Set the logged-in user as the owner of the movie

        // Save the movie to the database
        movieService.createMovie(movie);

        // Redirect to the user dashboard after movie creation
        return "redirect:/dashboard";
    }
}
