package at.Cinemaland.Controller.web;

import at.Cinemaland.Model.Actor;
import at.Cinemaland.Model.Movie;
import at.Cinemaland.Model.MovieCategory;
import at.Cinemaland.Service.ActorService;
import at.Cinemaland.Service.FileDataService;
import at.Cinemaland.Service.MovieCategoryService;
import at.Cinemaland.Service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * The personal administration area. The services only return the logged-in user's own data,
 * so an ID of another user's movie/category/actor is treated as "not found" (back to the dashboard).
 */
@Controller
@RequestMapping("/administration")
public class AdministrationController {

    private static final String DASHBOARD = "redirect:/administration";

    final MovieCategoryService movieCategoryService;
    final FileDataService fileDataService;
    final MovieService movieService;
    final ActorService actorService;

    public AdministrationController(MovieCategoryService movieCategoryService,
                                    FileDataService fileDataService,
                                    MovieService movieService,
                                    ActorService actorService) {
        this.movieCategoryService = movieCategoryService;
        this.fileDataService = fileDataService;
        this.movieService = movieService;
        this.actorService = actorService;
    }

    @GetMapping()
    public String administration(Model model) {
        List<Movie> movies = movieService.getMovies();
        List<MovieCategory> categories = movieCategoryService.getCategories();
        model.addAttribute("movies", movies);
        model.addAttribute("categories", categories);
        return "administration/administration";
    }

    @GetMapping("/category/create")
    public String createCategory(Model model) {
        model.addAttribute("categories", null);
        return "administration/create-category";
    }

    @GetMapping("/category/{id}/update")
    public String categoryUpdate(Model model, @PathVariable("id") String id) {
        MovieCategory category = movieCategoryService.getCategory(id);
        if (category == null) {
            return DASHBOARD;
        }
        model.addAttribute("category", category);
        return "administration/create-category";
    }

    @PostMapping("/category/{id}/update")
    public String updateCategorySubmit(@PathVariable("id") String id,
                                       @RequestParam("name") String name,
                                       @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        MovieCategory entity = movieCategoryService.updateCategory(id, name);
        if (entity != null) {
            fileDataService.replaceOrUpdateFileData(file, entity.getId());
        }
        return DASHBOARD;
    }

    @PostMapping("/category/{id}/delete")
    public String deleteCategory(@PathVariable("id") String id) {
        movieCategoryService.deleteCategory(id);
        return DASHBOARD;
    }

    @PostMapping("/category/create")
    public String createCategorySubmit(@RequestParam("name") String name, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        MovieCategory category = new MovieCategory(name);
        this.movieCategoryService.createCategory(category);
        this.fileDataService.createFileData(file, category.getId());
        return DASHBOARD;
    }

    @GetMapping("/movie/create")
    public String movieCreate(Model model) {
        model.addAttribute("categories", movieCategoryService.getCategories());
        return "administration/create-movie";
    }

    @PostMapping("/movie/create")
    public String createMovie(Movie movie,
                              @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Movie entity = this.movieService.createMovie(movie);
        fileDataService.createFileData(file, entity.getId()); // does nothing if no file was chosen
        // open the new movie in the editor so actors can be added right away
        return "redirect:/administration/movie/" + entity.getId() + "/update?created";
    }

    @GetMapping("/movie/{id}/update")
    public String updateMovie(Model model, @PathVariable("id") String id) {
        Movie movie = movieService.getMovie(id);
        if (movie == null) {
            return DASHBOARD;
        }
        model.addAttribute("categories", movieCategoryService.getCategories());
        model.addAttribute("movie", movie);
        return "administration/create-movie";
    }

    @PostMapping("/movie/{movieId}/update")
    public String updateMovieSubmit(
            Movie movie,
            @PathVariable("movieId") String movieId,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Movie entity = movieService.updateMovie(movieId, movie);
        if (entity != null) {
            fileDataService.replaceOrUpdateFileData(file, entity.getId());
        }
        return DASHBOARD;
    }

    @PostMapping("/movie/{id}/delete")
    public String deleteMovie(@PathVariable("id") String id) {
        movieService.deleteMovie(id);
        return DASHBOARD;
    }

    @PostMapping("/movie/{movieId}/actor/create")
    public String createActor(@PathVariable("movieId") String movieId, Actor actor, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        if (movieService.getMovie(movieId) == null) {
            return DASHBOARD;
        }
        Actor entity = actorService.createActor(actor);
        fileDataService.createFileData(file, entity.getId());
        movieService.addActors(movieId, Set.of(entity.getId()));
        return "redirect:/administration/movie/" + movieId + "/update";
    }

    @GetMapping("/movie/{movieId}/actor/create")
    public String actor(@PathVariable("movieId") String movieId, Model model) {
        Movie movie = movieService.getMovie(movieId);
        if (movie == null) {
            return DASHBOARD;
        }
        model.addAttribute("movie", movie);
        model.addAttribute("actor", null);
        return "administration/create-actor";
    }

    @GetMapping("/movie/{movieId}/actor/{actorId}/update")
    public String actorUpdate(@PathVariable("movieId") String movieId, @PathVariable("actorId") String actorId, Model model) {
        Movie movie = movieService.getMovie(movieId);
        Actor actor = actorService.getActor(actorId);
        if (movie == null || actor == null) {
            return DASHBOARD;
        }
        model.addAttribute("movie", movie);
        model.addAttribute("actor", actor);
        return "administration/create-actor";
    }

    @PostMapping("/movie/{movieId}/actor/{actorId}/update")
    public String updateActorSubmit(
            @PathVariable("movieId") String movieId,
            @PathVariable("actorId") String actorId,
            Actor actor,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Actor entity = actorService.updateActor(actorId, actor);
        if (entity != null) {
            fileDataService.replaceOrUpdateFileData(file, entity.getId());
        }
        return "redirect:/administration/movie/" + movieId + "/update";
    }

    @PostMapping("/movie/{movieId}/actor/{actorId}/delete")
    public String deleteActor(@PathVariable("movieId") String movieId, @PathVariable("actorId") String actorId) {
        actorService.deleteActor(actorId);
        return "redirect:/administration/movie/" + movieId + "/update";
    }
}
