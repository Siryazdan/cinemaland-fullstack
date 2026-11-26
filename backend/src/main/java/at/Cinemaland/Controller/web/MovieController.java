package at.Cinemaland.Controller.web;

import at.Cinemaland.Model.Movie;
import at.Cinemaland.Model.MovieCategory;
import at.Cinemaland.Service.ActorService;
import at.Cinemaland.Service.MovieCategoryService;
import at.Cinemaland.Service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequestMapping("")
public class MovieController {

    private static final int TOP_RATED_ON_HOME = 12;

    final MovieService movieService;
    final MovieCategoryService movieCategoryService;
    final ActorService actorService;

    public MovieController(MovieService movieService,
                           MovieCategoryService movieCategoryService,
                           ActorService actorService) {
        this.movieService = movieService;
        this.movieCategoryService = movieCategoryService;
        this.actorService = actorService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Movie> movies = movieService.getMovies().stream()
                .sorted(Comparator.comparing(Movie::getTitle, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .toList();
        List<Movie> topRated = movieService.getTopRatedMovies();

        model.addAttribute("movies", movies);
        model.addAttribute("categories", movieCategoryService.getCategories());
        model.addAttribute("topRated", topRated.stream().limit(TOP_RATED_ON_HOME).toList());
        model.addAttribute("actorCount", actorService.getActors().size());
        model.addAttribute("pick", pickOne(topRated.isEmpty() ? movies : topRated));
        return "index";
    }

    @GetMapping("/movie/{id}")
    public String getMovieDetails(@PathVariable("id") String id, Model model) {
        Movie movie = movieService.getMovie(id);
        if (movie == null) {
            return "redirect:/";
        }
        String youtubeTrailerLink = "https://www.youtube.com/results?search_query="
                + URLEncoder.encode(movie.getTitle() + " trailer", StandardCharsets.UTF_8);

        // a few other movies from the same category ("More like this")
        List<Movie> similar = movie.getCategory() == null ? List.of()
                : movieService.getMoviesByCategory(movie.getCategory().getName()).stream()
                .filter(m -> !m.getId().equals(movie.getId()))
                .limit(8)
                .toList();

        model.addAttribute("movie", movie);
        model.addAttribute("youtubeTrailerLink", youtubeTrailerLink);
        model.addAttribute("similar", similar);
        return "movie-details";
    }

    @GetMapping("/category/{categoryName}")
    public String getMoviesByCategory(@PathVariable("categoryName") String categoryName, Model model) {
        MovieCategory category = movieCategoryService.getCategories().stream()
                .filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElse(null);
        model.addAttribute("movies", movieService.getMoviesByCategory(categoryName));
        model.addAttribute("category", category);
        model.addAttribute("categoryName", category != null ? category.getName() : categoryName);
        return "category-details";
    }

    @GetMapping("/search")
    public String searchMovies(@RequestParam(value = "query", defaultValue = "") String query, Model model) {
        String trimmed = query.trim();
        if (trimmed.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("query", trimmed);
        model.addAttribute("movies", movieService.searchMovies(trimmed));
        return "search";
    }

    @GetMapping("/top-rated")
    public String getTopRatedMovies(Model model) {
        model.addAttribute("movies", movieService.getTopRatedMovies());
        return "top-rated-movies";
    }

    private static Movie pickOne(List<Movie> movies) {
        return movies.isEmpty() ? null : movies.get(ThreadLocalRandom.current().nextInt(movies.size()));
    }
}
