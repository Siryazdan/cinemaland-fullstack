package at.Cinemaland.Controller.api;


import at.Cinemaland.Model.MovieCategory;
import at.Cinemaland.Service.MovieCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/categories/")
public class MovieCategoryApiController {
    final MovieCategoryService movieCategoryService;

    public MovieCategoryApiController(MovieCategoryService movieCategoryService) {
        this.movieCategoryService = movieCategoryService;
    }
    @PostMapping()
    public MovieCategory createMovieCategory(@RequestBody MovieCategory category) {
        return movieCategoryService.createCategory(category);
    }
    @GetMapping()
    public Iterable<MovieCategory> getCategories() {
        return movieCategoryService.getCategories();
    }
    @GetMapping("{id}")
    public MovieCategory getCategory(@PathVariable("id") String id) {
        MovieCategory category = movieCategoryService.getCategory(id);
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        return category;
    }
    @PutMapping("{id}")
    public MovieCategory updateCategory(@PathVariable("id") String id, @RequestBody MovieCategory category) {
        MovieCategory updated = movieCategoryService.updateCategory(id, category.getName());
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        return updated;
    }
    @DeleteMapping("{id}")
    public void deleteCategory(@PathVariable("id") String id) {
        movieCategoryService.deleteCategory(id);
    }
}
