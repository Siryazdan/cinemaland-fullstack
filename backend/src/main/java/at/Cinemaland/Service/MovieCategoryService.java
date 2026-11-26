package at.Cinemaland.Service;

import at.Cinemaland.Model.Movie;
import at.Cinemaland.Model.MovieCategory;
import at.Cinemaland.Repository.MovieCategoryRepository;
import at.Cinemaland.Repository.MovieRepository;
import at.Cinemaland.Security.CurrentUser;
import org.springframework.stereotype.Service;

import java.util.List;

/** Categories of the logged-in user only. */
@Service
public class MovieCategoryService {

    private final MovieCategoryRepository movieCategoryRepository;
    private final FileDataService fileDataService;
    private final MovieRepository movieRepository;
    private final CurrentUser currentUser;

    public MovieCategoryService(MovieCategoryRepository movieCategoryRepository, FileDataService fileDataService,
                                MovieRepository movieRepository, CurrentUser currentUser) {
        this.movieCategoryRepository = movieCategoryRepository;
        this.fileDataService = fileDataService;
        this.movieRepository = movieRepository;
        this.currentUser = currentUser;
    }

    public List<MovieCategory> getCategories() {
        return movieCategoryRepository.findByUserId(currentUser.id());
    }

    public MovieCategory createCategory(MovieCategory category) {
        category.setId(null); // a new category always gets a new ID
        category.setUserId(currentUser.id());
        return movieCategoryRepository.save(category);
    }

    /** The category, or null if it doesn't exist or belongs to another user. */
    public MovieCategory getCategory(String id) {
        return movieCategoryRepository.findByIdAndUserId(id, currentUser.id()).orElse(null);
    }

    public MovieCategory updateCategory(String id, String name) {
        MovieCategory category = getCategory(id);
        if (category != null) {
            category.setName(name);
            return movieCategoryRepository.save(category);
        }
        return null;
    }

    /** Deletes the category if it belongs to the current user. Returns false if not found. */
    public boolean deleteCategory(String id) {
        MovieCategory category = getCategory(id);
        if (category == null) {
            return false;
        }
        // Movies in this category are kept, they just have no category anymore
        // (otherwise the database foreign key blocks the delete)
        List<Movie> movies = movieRepository.findByUserIdAndMovieCategoryId(currentUser.id(), id);
        for (Movie movie : movies) {
            movie.setMovieCategoryId(null);
            movie.setCategory(null);
        }
        movieRepository.saveAll(movies);
        fileDataService.deleteFileByRelId(id);
        movieCategoryRepository.delete(category);
        return true;
    }
}
