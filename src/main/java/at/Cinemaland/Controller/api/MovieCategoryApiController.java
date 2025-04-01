package at.Cinemaland.Controller.api;


import at.Cinemaland.Model.MovieCategory;
import at.Cinemaland.Service.MovieCategoryService;
import org.springframework.web.bind.annotation.*;

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
    public MovieCategory getCategory(@PathVariable String id) {
        return movieCategoryService.getCategory(id);
    }
    @PutMapping("{id}")
    public MovieCategory updateCategory(@PathVariable String id, @RequestBody MovieCategory category) {
        return movieCategoryService.updateCategory(id, category.getName());
    }
    @DeleteMapping("{id}")
    public void deleteCategory(@PathVariable String id) {
        movieCategoryService.deleteCategory(id);
    }
}
