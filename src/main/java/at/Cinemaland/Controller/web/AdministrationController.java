package at.Cinemaland.Controller.web;

import at.Cinemaland.Model.Movie;
import at.Cinemaland.Model.MovieCategory;
import at.Cinemaland.Service.FileDataService;
import at.Cinemaland.Service.MovieCategoryService;
import at.Cinemaland.Service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/administration")
public class AdministrationController {

    final MovieCategoryService movieCategoryService;
    final FileDataService fileDataService;
    final MovieService movieService;

    public AdministrationController(MovieCategoryService movieCategoryService, FileDataService fileDataService, MovieService movieService) {
        this.movieCategoryService = movieCategoryService;
        this.fileDataService = fileDataService;
        this.movieService = movieService;
    }

    @GetMapping()
    public String administration(Model model) {

        List<Movie> movies = movieService.getMovies();
        List<MovieCategory> categories = movieCategoryService.getCategories();

        model.addAttribute("movies", movies);
        model.addAttribute("categories", categories);

        return "administration/administration";
    }

    @GetMapping("/movie/create")
    public String createMovie(Model model) {

        model.addAttribute("movie", new Movie());
        return "administration/movie/create";
    }
}


